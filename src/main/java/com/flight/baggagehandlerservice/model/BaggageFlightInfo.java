package com.flight.baggagehandlerservice.model;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class BaggageFlightInfo {

	String bagUId;
	String bagAPC;
	String airline;
	String flight;
	LocalDateTime departureTime;
	String destination;
}
