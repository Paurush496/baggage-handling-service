package com.flight.baggagehandlerservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

import com.flight.baggagehandlerservice.entity.CheckedInBaggage;
import com.flight.baggagehandlerservice.model.BaggageFlightInfo;
import com.flight.baggagehandlerservice.model.CheckInBaggage;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CheckInBaggageMapper {
	
	CheckInBaggageMapper INSTANCE = Mappers.getMapper( CheckInBaggageMapper.class ); 

	CheckedInBaggage toCheckedInBaggageEntity(CheckInBaggage baggageToCheckIn);
	
	BaggageFlightInfo toBaggageFlightInfo(CheckedInBaggage checkedInBaggage);
}
