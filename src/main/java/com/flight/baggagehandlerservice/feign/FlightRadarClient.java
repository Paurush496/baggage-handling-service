package com.flight.baggagehandlerservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

import com.flight.baggagehandlerservice.configuration.FeignClientConfig;
import com.flight.baggagehandlerservice.model.AllAirlines;
import com.flight.baggagehandlerservice.model.AllAirlinesLogos;

@FeignClient(name = "flight-radar", url = "${rapidapi.flight.radar.uri}", configuration = FeignClientConfig.class)
public interface FlightRadarClient {

	@GetMapping(value = "/airlines/list", consumes = MediaType.APPLICATION_JSON_VALUE, headers = {
			"X-RapidAPI-Key=${rapidapi.apikey}", "X-RapidAPI-Host=${rapidapi.flight.radar.host}" })
	AllAirlines fetchAirlines();

	@GetMapping(value = "/airlines/get-logos", consumes = MediaType.APPLICATION_JSON_VALUE, headers = {
			"X-RapidAPI-Key=${rapidapi.apikey}", "X-RapidAPI-Host=${rapidapi.flight.radar.host}" })
	AllAirlinesLogos fetchAirlinesLogos();

}
