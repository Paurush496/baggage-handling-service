package com.flight.baggagehandlerservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.flight.baggagehandlerservice.configuration.FeignClientConfig;
import com.flight.baggagehandlerservice.model.AllFlightsScheduled;

@FeignClient(name = "flight-info", url = "${rapidapi.flight.info.uri}", configuration = FeignClientConfig.class)
public interface FlightInfoClient {

	@GetMapping(value = "/schedules", consumes = MediaType.APPLICATION_JSON_VALUE, headers = {
			"X-RapidAPI-Key=${rapidapi.apikey}", "X-RapidAPI-Host=${rapidapi.flight.info.host}" })
	AllFlightsScheduled getFlightsScheduledAtAnAirport(@RequestParam("version") String apiVersion,
			@RequestParam("DepartureDateTime") String departureTime,
			@RequestParam("DepartureAirport") String airportCode, @RequestParam("CodeType") String airportCodeType);
}
