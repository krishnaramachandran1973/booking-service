package com.cts.service.impl;

import static com.cts.util.LocalDateToString.dateToString;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.cts.configuration.FareServiceProxy;
import com.cts.domain.BookingRecord;
import com.cts.domain.BookingStatus;
import com.cts.domain.Inventory;
import com.cts.domain.Passenger;
import com.cts.errors.BookingException;
import com.cts.repository.BookingRepository;
import com.cts.repository.InventoryRepository;
import com.cts.service.BookingService;
import com.cts.vo.Fare;
import com.cts.vo.NewBooking;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional
@EnableFeignClients(basePackages = "com.cts.configuration")
public class BookingServiceImpl implements BookingService {
	private static final Logger logger = LoggerFactory.getLogger(BookingServiceImpl.class);

	private final RestTemplate restTemplate;
	private final InventoryRepository inventoryRepository;
	private final BookingRepository bookingRepository;
	private final RabbitTemplate rabbitTemplate;
	private final FareServiceProxy fareProxy;

	@Value("${booking.fare-url}")
	String fareUrl;

	@Value("${booking.amqp-search-queue}")
	String queue;

	@Override
	public Long book(BookingRecord record) {
		logger.info("Booking for {}", record);
		logger.info(">> Calling FareService");

		UriComponentsBuilder fareUrlBuilder = UriComponentsBuilder.fromHttpUrl(fareUrl)
				.queryParam("flightNumber", record.getFlightNumber())
				.queryParam("flightDate", dateToString(record.getFlightDate()));
		
		Fare fare = restTemplate.getForObject(fareUrlBuilder.toUriString(), Fare.class);

		//Fare fare = fareProxy.getFare(record.getFlightNumber(), dateToString(record.getFlightDate()));

		logger.info(">> Fare received from FareService " + fare);

		if (!record.getAmount()
				.equals(fare.getAmount())) {
			throw new BookingException("Fare is tampered");
		}

		logger.info("Checking inventory for available seats");
		Inventory inventory = inventoryRepository.findByFlightNumberAndFlightDate(record.getFlightNumber(),
				record.getFlightDate());

		if (!inventory.isAvailable(record.getPassengers()
				.size())) {
			logger.info("No available seats");
			throw new BookingException("No more seats avaialble");
		}

		logger.info("Seats available. Proceeding...");
		inventory.setSeats(inventory.getSeats() - record.getPassengers()
				.size());
		inventoryRepository.saveAndFlush(inventory);
		logger.info("sucessfully updated inventory");

		record.setStatus(BookingStatus.BOOKING_CONFIRMED);
		List<Passenger> passengers = record.getPassengers();
		passengers.forEach(passenger -> passenger.setBookingRecord(record));
		record.setBookingDate(LocalDate.now());
		Long id = bookingRepository.save(record)
				.getId();

		logger.info("Sending Booking event to queue {}", queue);
		NewBooking newBooking = NewBooking.builder()
				.flightNumber(record.getFlightNumber())
				.flightDate(record.getFlightDate())
				.seats(inventory.getSeats())
				.build();
		rabbitTemplate.convertAndSend(queue, newBooking);

		return id;
	}

	@Override
	public Optional<BookingRecord> getBooking(Long id) {
		return bookingRepository.findById(id);
	}

	@Override
	public void updateStatus(BookingStatus status, Long bookingId) {
		logger.info("Booking id {} updating", bookingId);
		BookingRecord record = bookingRepository.findById(bookingId)
				.get();
		record.setStatus(status);

	}

}
