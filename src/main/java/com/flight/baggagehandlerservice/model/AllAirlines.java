package com.flight.baggagehandlerservice.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class AllAirlines {

	@JsonProperty(value = "rows")
	private List<Airline> airlines;
}
