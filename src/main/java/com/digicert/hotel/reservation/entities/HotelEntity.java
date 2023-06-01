package com.digicert.hotel.reservation.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity

@Table(name = "hotel")
public class HotelEntity {
	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;

	@Size(max = 255)
	@NotNull
	@Column(name = "Name", nullable = false)
	private String name;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}