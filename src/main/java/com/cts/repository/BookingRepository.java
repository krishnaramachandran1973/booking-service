package com.cts.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.domain.BookingRecord;

public interface BookingRepository extends JpaRepository<BookingRecord, Long>{
}
