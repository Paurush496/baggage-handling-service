package com.flight.baggagehandlerservice.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "checked_in_baggage")
public class CheckedInBaggage implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "row_id", nullable = false)
	private Long rowId;
	
	@Column(name = "bag_uid")
	private String bagUId;
	
	@Column(name = "APC")
	private String bagAPC;
	
	@Column(name = "bag_user_id")
	private String userId;
	
	@Column(name = "bag_weight_kg")
	private float weight;
	
	@Column(name = "check_in_time")
	private LocalDateTime checkInTime;
	
	@Column(name = "screening_passed")
	private Boolean isScreeningCleared;
	
	@Column(name = "current_status")
	private String status;
	
	@Column(name = "airline")
	private String airline;
	
	@Column(name = "flight")
	private String flight;
	
	@Column(name = "departure")
	private LocalDateTime departureTime;
	
	@Column(name = "admin_comment")
	private String adminComment;
}
