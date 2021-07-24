package com.cts.service;

import java.util.Optional;

import com.cts.domain.BookingRecord;
import com.cts.domain.BookingStatus;

public interface BookingService {
	public Long book(BookingRecord record);

	public Optional<BookingRecord> getBooking(Long id);

	public void updateStatus(BookingStatus status, Long bookingId);

}
