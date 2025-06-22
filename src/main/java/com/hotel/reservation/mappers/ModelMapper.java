package com.hotel.reservation.mappers;

import com.hotel.reservation.api.model.Customer;
import com.hotel.reservation.api.model.Hotel;
import com.hotel.reservation.api.model.Reservation;
import com.hotel.reservation.entities.CustomerEntity;
import com.hotel.reservation.entities.HotelEntity;
import com.hotel.reservation.entities.ReservationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ModelMapper {

	public static ModelMapper INSTANCE = Mappers.getMapper(ModelMapper.class);

	@Mapping(target="hotel", source="hotelEntity")
	@Mapping(target="customer", source="customerEntity")
	Reservation reservationEntityToDTO(ReservationEntity reservationEntity);


	@Mapping(target="hotelEntity", source="hotel")
	@Mapping(target="customerEntity", source="customer")
	ReservationEntity reservationDTOToEntity(Reservation reservationDTO);

	Hotel hotelEntityToDTO(HotelEntity hotelEntity);
	Customer customerEntityToDTO(CustomerEntity customerEntity);
	}
