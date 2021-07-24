package com.cts.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.domain.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
	Inventory findByFlightNumberAndFlightDate(String flightNumber, LocalDate flightDate);
}
