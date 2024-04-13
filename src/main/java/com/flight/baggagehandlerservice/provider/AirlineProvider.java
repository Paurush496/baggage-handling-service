package com.flight.baggagehandlerservice.provider;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flight.baggagehandlerservice.feign.FlightRadarClient;
import com.flight.baggagehandlerservice.model.Airline;
import com.flight.baggagehandlerservice.model.AllAirlines;

import lombok.Data;

@Data
@Service
public class AirlineProvider {

	@Autowired
	private FlightRadarClient radarClient;

	private static AllAirlines allAirlines;

	public String getNameFromIATAAndICAOCode(Airline airlineObject) {
		if (allAirlines == null || allAirlines.getAirlines() == null || allAirlines.getAirlines().isEmpty()) {
			allAirlines = radarClient.fetchAirlines();
		}
		return allAirlines.getAirlines().stream()
				.filter(airline -> airlineObject.getCodeIATA().equalsIgnoreCase(airline.getCodeIATA())
						&& airlineObject.getCodeICAO().equalsIgnoreCase(airline.getCodeICAO()))
				.findFirst().map(Airline::getName)
				.orElseThrow(() -> new NoSuchElementException("No Airline found with given code."));
	}
}
