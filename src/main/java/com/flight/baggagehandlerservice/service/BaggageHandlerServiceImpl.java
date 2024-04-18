package com.flight.baggagehandlerservice.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.flight.baggagehandlerservice.entity.CheckedInBaggage;
import com.flight.baggagehandlerservice.feign.FlightInfoClient;
import com.flight.baggagehandlerservice.model.AllFlightsScheduled;
import com.flight.baggagehandlerservice.model.BaggageFlightInfo;
import com.flight.baggagehandlerservice.model.CheckInBaggage;
import com.flight.baggagehandlerservice.model.FlightSchedule;
import com.flight.baggagehandlerservice.provider.AirlineProvider;
import com.flight.baggagehandlerservice.repository.CheckedInBaggageRepository;

@Service
public class BaggageHandlerServiceImpl implements BaggageHandlerService {

	private final FlightInfoClient flightInfoClient;
	private final AirlineProvider airlineProvider;
	private final CheckedInBaggageRepository baggageRepository;

	public BaggageHandlerServiceImpl(FlightInfoClient flightInfoClient, AirlineProvider airlineProvider,
			CheckedInBaggageRepository baggageRepository) {
		this.flightInfoClient = flightInfoClient;
		this.airlineProvider = airlineProvider;
		this.baggageRepository = baggageRepository;
	}

	@Override
	public List<BaggageFlightInfo> validateAndCheckInBaggages(List<CheckInBaggage> baggages) {
		if (baggages == null || baggages.isEmpty()) {
			return List.of();
		}
		DateTimeFormatter inputPattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		String fromTime = LocalDateTime.now().format(inputPattern);
		String toTime = LocalDateTime.now().plusHours(10).format(inputPattern);

		List<BaggageFlightInfo> baggagesWithFlightInfo = new ArrayList<>();
		AllFlightsScheduled allFlights = flightInfoClient.getFlightsScheduledAtAnAirport("v2", fromTime + "/" + toTime,
				"BOM", "IATA");

		baggages.forEach(bag -> {

			String codeIATA = bag.getBagAPC().substring(0, 2);
			int flightNo = Integer.parseInt(bag.getBagAPC().substring(2, 6));

			FlightSchedule flightInfo = allFlights.getFlightsScheduled().stream()
					.filter(flight -> codeIATA.equalsIgnoreCase(flight.getCarrier().getCodeIATA())
							&& flightNo == flight.getFlightNumber())
					.findFirst()
					.orElseThrow(() -> new NoSuchElementException("No Flights scheduled with given APC tag"));

			String airlineName = airlineProvider.getNameFromIATAAndICAOCode(flightInfo.getCarrier());
			String flight = codeIATA.concat("-" + flightNo);

			// TODO Below mappings using Mapstruct interface which is currently not working
			CheckedInBaggage checkedInBaggage = new CheckedInBaggage();
			checkedInBaggage.setBagUId(bag.getBagUId());
			checkedInBaggage.setBagAPC(bag.getBagAPC());
			checkedInBaggage.setWeight(bag.getWeight());
			checkedInBaggage.setUserId(bag.getUserId());
			checkedInBaggage.setCheckInTime(bag.getCheckInTime());
			checkedInBaggage.setAirline(airlineName);
			checkedInBaggage.setFlight(flight);
			checkedInBaggage.setDepartureTime(flightInfo.getDepartureTime());
			checkedInBaggage.setLogoUrl(airlineProvider.getLogoUrlFromIATAAndICAOCode(flightInfo.getCarrier()));
			baggageRepository.saveAndFlush(checkedInBaggage);

			BaggageFlightInfo baggageFlightInfo = new BaggageFlightInfo();
			baggageFlightInfo.setBagUId(bag.getBagUId());
			baggageFlightInfo.setBagAPC(bag.getBagAPC());
			baggageFlightInfo.setAirline(airlineName);
			baggageFlightInfo.setFlight(flight);
			baggageFlightInfo.setDepartureTime(flightInfo.getDepartureTime());

			baggagesWithFlightInfo.add(baggageFlightInfo);
		});
		return baggagesWithFlightInfo;
	}

}
