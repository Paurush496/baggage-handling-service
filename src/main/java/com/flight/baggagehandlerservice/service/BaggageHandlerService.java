package com.flight.baggagehandlerservice.service;

import java.util.List;

import com.flight.baggagehandlerservice.model.BaggageFlightInfo;
import com.flight.baggagehandlerservice.model.CheckInBaggage;

public interface BaggageHandlerService {

	List<BaggageFlightInfo> validateAndCheckInBaggages(List<CheckInBaggage> baggages);
}
