package com.digicert.hotel.reservation.controller;

import com.digicert.hotel.reservation.api.HotelApiDelegate;
import com.digicert.hotel.reservation.api.model.Hotel;
import com.digicert.hotel.reservation.api.model.HotelList;
import com.digicert.hotel.reservation.mappers.ModelMapper;
import com.digicert.hotel.reservation.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HotelApiDelegateImpl implements HotelApiDelegate {

	@Autowired
	HotelRepository hotelRepository;

	@Override
	public ResponseEntity<HotelList> getHotels(Pageable pageable) {
		try {
			List<Hotel> hotelsDTO = new ArrayList<Hotel>();
			hotelRepository.findAll(pageable).forEach(hotel -> hotelsDTO.add(ModelMapper.INSTANCE.hotelEntityToDTO(hotel)));
			HotelList hotelList = new HotelList();
			hotelList.setHotels(hotelsDTO);
			return new ResponseEntity<HotelList>(hotelList, HttpStatus.OK);
		} catch (Throwable t) {
			throw t;
		}
	}

}
