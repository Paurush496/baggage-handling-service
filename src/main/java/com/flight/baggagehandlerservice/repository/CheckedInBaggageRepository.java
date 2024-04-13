package com.flight.baggagehandlerservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.flight.baggagehandlerservice.entity.CheckedInBaggage;

@Repository
public interface CheckedInBaggageRepository extends JpaRepository<CheckedInBaggage, Long> {

}
