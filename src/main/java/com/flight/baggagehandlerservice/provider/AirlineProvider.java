package com.flight.baggagehandlerservice.provider;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flight.baggagehandlerservice.feign.FlightRadarClient;
import com.flight.baggagehandlerservice.model.Airline;
import com.flight.baggagehandlerservice.model.AllAirlines;
import com.flight.baggagehandlerservice.model.AllAirlinesLogos;

import lombok.Data;

@Data
@Service
public class AirlineProvider {

	@Autowired
	private FlightRadarClient radarClient;

	private static AllAirlines allAirlines;
	private static AllAirlinesLogos airlinesLogos;
	private static final String DEFAULT_LOGO_URL = "https://www.shutterstock.com/image-vector/airplane-icon-vector-transportation-logo-600nw-1283834365.jpg";

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

	public String getLogoUrlFromIATAAndICAOCode(Airline airlineObject) {
		if (airlinesLogos == null || airlinesLogos.getLogotypes() == null || airlinesLogos.getLogotypes().isEmpty()) {
			airlinesLogos = radarClient.fetchAirlinesLogos();
		}
		return airlinesLogos.getLogotypes().stream()
				.filter(airlineLogo -> airlineLogo.getImageName().contains(airlineObject.getCodeIATA())
						&& airlineLogo.getImageName().contains(airlineObject.getCodeICAO()))
				.findFirst().map(AllAirlinesLogos.Logo::getUrl).orElse(DEFAULT_LOGO_URL);
	}
}
