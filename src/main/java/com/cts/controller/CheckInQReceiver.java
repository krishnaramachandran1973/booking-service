package com.cts.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.cts.domain.BookingStatus;
import com.cts.service.BookingService;
import com.cts.vo.BookingUpdate;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class CheckInQReceiver {
	private static final Logger logger = LoggerFactory.getLogger(CheckInQReceiver.class);

	private final BookingService bookingService;
	

	@RabbitListener(queues = "CheckInQ")
	public void processMessage(BookingUpdate update) {
		logger.info("Booking Id {} received for Checkin update", update.getBookingId());
		bookingService.updateStatus(BookingStatus.CHECKED_IN, update.getBookingId());
	}

}
