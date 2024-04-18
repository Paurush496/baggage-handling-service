package com.flight.baggagehandlerservice.provider;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flight.baggagehandlerservice.feign.FlightRadarClient;
import com.flight.baggagehandlerservice.model.Airline;
import com.flight.baggagehandlerservice.model.AllAirlines;
import com.flight.baggagehandlerservice.model.AllAirlinesLogos;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Service
public class AirlineProvider {

	@Autowired
	private FlightRadarClient radarClient;

	private static AllAirlines allAirlines;
	private static AllAirlinesLogos airlinesLogos;
	private static final String DEFAULT_LOGO_URL = "https://img.icons8.com/bubbles/100/airplane-take-off.png";

	public String getNameFromIATAAndICAOCode(Airline airlineObject) {
		if (allAirlines == null || allAirlines.getAirlines() == null || allAirlines.getAirlines().isEmpty()) {
			log.info("Calling Radar Client Airlines API");
			allAirlines = radarClient.fetchAirlines();
		}
		log.info("Fetching Airline name for IATA code {} and ICAO code {}", airlineObject.getCodeIATA(),
				airlineObject.getCodeICAO());
		return allAirlines.getAirlines().stream()
				.filter(airline -> airlineObject.getCodeIATA().equalsIgnoreCase(airline.getCodeIATA())
						&& airlineObject.getCodeICAO().equalsIgnoreCase(airline.getCodeICAO()))
				.findFirst().map(Airline::getName).orElseThrow(() -> new NoSuchElementException(
						"No Airline found with given code: " + airlineObject.getCodeIATA()));
	}

	public String getLogoUrlFromIATAAndICAOCode(Airline airlineObject) {
		if (airlinesLogos == null || airlinesLogos.getLogotypes() == null || airlinesLogos.getLogotypes().isEmpty()) {
			log.info("Calling Radar Client Airlines Logos API");
			airlinesLogos = radarClient.fetchAirlinesLogos();
		}
		log.info("Fetching logo for Airline {}", airlineObject.getName());
		return airlinesLogos.getLogotypes().stream()
				.filter(airlineLogo -> airlineLogo.getImageName().contains(airlineObject.getCodeIATA())
						&& airlineLogo.getImageName().contains(airlineObject.getCodeICAO()))
				.findFirst().map(AllAirlinesLogos.Logo::getUrl).orElse(DEFAULT_LOGO_URL);
	}
}
