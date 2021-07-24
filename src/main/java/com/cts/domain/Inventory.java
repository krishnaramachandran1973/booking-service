package com.cts.domain;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor(force = true)
@AllArgsConstructor
@Data
@Builder
@Entity
public class Inventory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;

	private String flightNumber;

	private LocalDate flightDate;

	private Boolean available;

	private int seats;

	public boolean isAvailable(int count) {
		return ((seats - count) > 5);
	}

}
