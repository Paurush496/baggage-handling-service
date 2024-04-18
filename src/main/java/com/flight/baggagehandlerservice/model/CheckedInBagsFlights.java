package com.flight.baggagehandlerservice.model;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CheckedInBagsFlights {

	int bagsCount;
	String airline;
	String flight;
	String airlinesLogo;
	LocalDateTime departure;
}
