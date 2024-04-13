package com.flight.baggagehandlerservice.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class AllFlightsScheduled {
	
	@JsonProperty(value = "data")
	private List<FlightSchedule> flightsScheduled;
}
