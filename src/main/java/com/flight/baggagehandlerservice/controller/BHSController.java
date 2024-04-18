package com.flight.baggagehandlerservice.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flight.baggagehandlerservice.model.BaggageFlightInfo;
import com.flight.baggagehandlerservice.model.CheckInBaggage;
import com.flight.baggagehandlerservice.service.BaggageHandlerService;

@RestController
@RequestMapping(path = "/bhs")
//@CrossOrigin(origins = "http://localhost:4200")
public class BHSController {

	private final BaggageHandlerService baggageHandlerService;

	public BHSController(BaggageHandlerService baggageHandlerService) {
		this.baggageHandlerService = baggageHandlerService;
	}

	@PostMapping(path = "/check-in-baggage")
	public ResponseEntity<List<BaggageFlightInfo>> checkInBaggage(@RequestBody List<CheckInBaggage> baggages) {
		List<BaggageFlightInfo> checkedInBaggagesWithFlightInfo = baggageHandlerService
				.validateAndCheckInBaggages(baggages);
		return checkedInBaggagesWithFlightInfo != null && !checkedInBaggagesWithFlightInfo.isEmpty()
				? ResponseEntity.ok(checkedInBaggagesWithFlightInfo)
				: ResponseEntity.badRequest().build();
	}
}
