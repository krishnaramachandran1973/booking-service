package com.cts;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import com.cts.domain.BookingRecord;
import com.cts.domain.Inventory;
import com.cts.domain.Passenger;
import com.cts.repository.InventoryRepository;
import com.cts.service.BookingService;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableDiscoveryClient
@EnableSwagger2
@SpringBootApplication
public class BookingServiceApplication {
	private static final Logger logger = LoggerFactory.getLogger(BookingServiceApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(BookingServiceApplication.class, args);
	}

	@Bean
	CommandLineRunner init(InventoryRepository inventoryRepository, BookingService bookingService) {
		return args -> {

			Arrays.asList(Inventory.builder()
					.flightNumber("BF100")
					.flightDate(LocalDate.of(2021, 8, 22))
					.seats(100)
					.build(),
					Inventory.builder()
							.flightNumber("BF101")
							.flightDate(LocalDate.of(2021, 9, 18))
							.seats(100)
							.build(),
					Inventory.builder()
							.flightNumber("BF102")
							.flightDate(LocalDate.of(2021, 10, 12))
							.seats(100)
							.build(),
					Inventory.builder()
							.flightNumber("BF103")
							.flightDate(LocalDate.of(2021, 11, 05))
							.seats(100)
							.build(),
					Inventory.builder()
							.flightNumber("BF104")
							.flightDate(LocalDate.of(2021, 12, 01))
							.seats(100)
							.build(),
					Inventory.builder()
							.flightNumber("BF105")
							.flightDate(LocalDate.of(2021, 8, 16))
							.seats(100)
							.build(),
					Inventory.builder()
							.flightNumber("BF106")
							.flightDate(LocalDate.of(2021, 8, 19))
							.seats(100)
							.build())
					.forEach(inventory -> inventoryRepository.save(inventory));

			BookingRecord bookingRecord = BookingRecord.builder()
					.flightNumber("BF101")
					.origin("NYC")
					.destination("SFO")
					.flightDate(LocalDate.of(2021, 9, 18))
					.bookingDate(LocalDate.of(2021, 7, 01))
					.amount("101")
					.build();

			List<Passenger> passengers = Arrays.asList(Passenger.builder()
					.firstName("Jean")
					.lastName("Franc")
					.gender("Male")
					.bookingRecord(bookingRecord)
					.build(),
					Passenger.builder()
							.firstName("Redi")
							.lastName("Inav")
							.gender("Feale")
							.bookingRecord(bookingRecord)
							.build());

			bookingRecord.setPassengers(passengers);

			Long record = bookingService.book(bookingRecord);
			logger.info("Booking successfully saved..." + record);

			logger.info("Looking to load booking record...");
			logger.info("Result: " + bookingService.getBooking(record));
		};
	}

}
