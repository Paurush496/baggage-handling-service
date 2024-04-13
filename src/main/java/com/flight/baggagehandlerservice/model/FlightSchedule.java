package com.flight.baggagehandlerservice.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class FlightSchedule {

	private Airline carrier;
	private int flightNumber;
	private String flightType;
	private LocalDateTime departureTime;

	@SuppressWarnings("unchecked")
	@JsonProperty("departure")
	private void unpackDeparture(Map<String, Object> departue) {
		Map<String, String> departTimes = (Map<String, String>) departue.get("time");
		this.departureTime = LocalDateTime.of(LocalDate.now(), LocalTime.parse(departTimes.get("local")));
	}
}