package com.cts.errors;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;

@Builder
public class BookingError {
	
	@Getter
	private String message;

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@Getter
	@Default
	private List<String> errors = new ArrayList<>();

}
