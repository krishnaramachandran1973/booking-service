package com.cts.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateToString {
	public static String dateToString(LocalDate date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		return date.format(formatter);
	}

}
