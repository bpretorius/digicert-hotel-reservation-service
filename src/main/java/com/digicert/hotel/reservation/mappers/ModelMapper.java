package com.digicert.hotel.reservation.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ModelMapper {

	public static ModelMapper INSTANCE = Mappers.getMapper(ModelMapper.class);

	@Mapping(target="hotel", source="hotelEntity")
	@Mapping(target="customer", source="customerEntity")
	com.digicert.hotel.reservation.api.model.Reservation reservationEntityToDTO(com.digicert.hotel.reservation.entities.ReservationEntity reservationEntity);


	@Mapping(target="hotelEntity", source="hotel")
	@Mapping(target="customerEntity", source="customer")
	com.digicert.hotel.reservation.entities.ReservationEntity reservationDTOToEntity(com.digicert.hotel.reservation.api.model.Reservation reservationDTO);

	com.digicert.hotel.reservation.api.model.Hotel hotelEntityToDTO(com.digicert.hotel.reservation.entities.HotelEntity hotelEntity);
	com.digicert.hotel.reservation.api.model.Customer customerEntityToDTO(com.digicert.hotel.reservation.entities.CustomerEntity customerEntity);
	}
