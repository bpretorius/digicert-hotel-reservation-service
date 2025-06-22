package com.hotel.reservation.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;

@Entity

@Table(name = "reservation")
public class ReservationEntity {
	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@OneToOne
	private HotelEntity hotelEntity;

	@NotNull
	@OneToOne
	private CustomerEntity customerEntity;

	@NotNull
	@Size(max = 255)
	@Column(name = "ReservationReference")
	private String reservationReference;

	@NotNull
	private Integer numberOfAdults;

	@NotNull
	private Integer numberOfChildren;

	@Column(name = "FromDate")
	private Instant fromDate;

	@Column(name = "ToDate")
	private Instant toDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public HotelEntity getHotelEntity() {
		return hotelEntity;
	}

	public void setHotelEntity(HotelEntity hotelEntity) {
		this.hotelEntity = hotelEntity;
	}

	public CustomerEntity getCustomerEntity() {
		return customerEntity;
	}

	public void setCustomerEntity(CustomerEntity customerEntity) {
		this.customerEntity = customerEntity;
	}

	public String getReservationReference() {
		return reservationReference;
	}

	public void setReservationReference(String reservationReference) {
		this.reservationReference = reservationReference;
	}

	public Instant getFromDate() {
		return fromDate;
	}

	public void setFromDate(Instant fromDate) {
		this.fromDate = fromDate;
	}

	public Instant getToDate() {
		return toDate;
	}

	public void setToDate(Instant toDate) {
		this.toDate = toDate;
	}

	public Integer getNumberOfAdults() {
		return numberOfAdults;
	}

	public void setNumberOfAdults(Integer numberOfAdults) {
		this.numberOfAdults = numberOfAdults;
	}

	public Integer getNumberOfChildren() {
		return numberOfChildren;
	}

	public void setNumberOfChildren(Integer numberOfChildren) {
		this.numberOfChildren = numberOfChildren;
	}
}