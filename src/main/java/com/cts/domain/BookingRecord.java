package com.cts.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ApiModel(description = "The BookingRecord")
@NoArgsConstructor(force = true)
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Data
@Entity
public class BookingRecord {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ApiModelProperty(notes = "The Flight Number")
	@NotNull
	private final String flightNumber;

	@NotNull
	private final String origin;

	@NotNull
	private final String destination;

	@NotNull
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private final LocalDate flightDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private LocalDate bookingDate;

	@NotNull
	private final String amount;

	@Enumerated(EnumType.STRING)
	private BookingStatus status;

	@NotNull
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@Default
	@JsonProperty(access = Access.READ_WRITE)
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST, mappedBy = "bookingRecord")
	List<Passenger> passengers = new ArrayList<>();

}
