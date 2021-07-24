package com.cts.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.domain.BookingRecord;
import com.cts.errors.BookingError;
import com.cts.service.BookingService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/booking")
public class BookingController {
	private static final Logger logger = LoggerFactory.getLogger(BookingController.class);

	@Autowired
	private BookingService service;

	@ApiOperation(value = "Receives BookingRecord for Flights", notes = "Requires a BookingRecord to proceed", response = Long.class)
	@PostMapping
	public ResponseEntity<?> book(@RequestBody @Valid BookingRecord bookingRecord, Errors errors) {
		logger.info("Creating a BookingRecord");
		if (errors.hasErrors()) {
			logger.info("Bad request for BookingRecord");
			List<String> ers = new ArrayList<>();
			errors.getFieldErrors()
					.forEach(er -> ers.add(er.getField() + ": " + er.getDefaultMessage()));
			return ResponseEntity.badRequest()
					.body(BookingError.builder()
							.message("Invalid booking Record")
							.errors(ers)
							.build());
		}
		return ResponseEntity.ok(service.book(bookingRecord));
	}

	@GetMapping("/{id}")
	public BookingRecord getBooking(@PathVariable Long id) {
		logger.info("Id as is >>> {}", id);
		Optional<BookingRecord> br = this.service.getBooking(id);
		if (br.isPresent()) {
			return br.get();
		}
		return new BookingRecord();
	}

	@ExceptionHandler
	public ResponseEntity<BookingError> handleException(Exception e) {
		return ResponseEntity.badRequest()
				.body(BookingError.builder()
						.message(e.getLocalizedMessage())
						.build());
	}
}
