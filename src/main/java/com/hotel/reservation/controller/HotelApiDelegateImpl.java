package com.hotel.reservation.controller;

import com.hotel.reservation.api.HotelApiDelegate;
import com.hotel.reservation.api.model.*;
import com.hotel.reservation.entities.CustomerEntity;
import com.hotel.reservation.entities.HotelEntity;
import com.hotel.reservation.entities.ReservationEntity;
import com.hotel.reservation.mappers.ModelMapper;
import com.hotel.reservation.repository.CustomerRepository;
import com.hotel.reservation.repository.HotelRepository;
import com.hotel.reservation.repository.ReservationRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class HotelApiDelegateImpl implements HotelApiDelegate {

	@Autowired
	ReservationRepository reservationRepository;

	@Autowired
	HotelRepository hotelRepository;

	@Autowired
	CustomerRepository customerRepository;

	/* 	Reason setting up data like this is as the data.sql onload was conflicting when testing.
		In Test the scheme had not yet been created before loading. Setting datasource.data: anotherfilename.sql
		was ignored
	*/
	@PostConstruct
	private void postConstruct() {

		CustomerEntity customerEntity = new CustomerEntity();
		customerEntity.setName("Robert Pretorius");
		customerRepository.save(customerEntity);
		customerEntity = new CustomerEntity();
		customerEntity.setName("John Smith");
		customerRepository.save(customerEntity);
		customerEntity = new CustomerEntity();
		customerEntity.setName("Jane Doe");
		customerRepository.save(customerEntity);

		HotelEntity hotelEntity = new HotelEntity();
		hotelEntity.setName("Hilton");
		hotelRepository.save(hotelEntity);
		hotelEntity = new HotelEntity();
		hotelEntity.setName("Southern Sun");
		hotelRepository.save(hotelEntity);
		hotelEntity = new HotelEntity();
		hotelEntity.setName("Protea");
		hotelRepository.save(hotelEntity);

	}

	@Override
	public ResponseEntity<Reservation> createReservation(Reservation reservationDTO) {
		try {

			ReservationEntity reservationEntity = ModelMapper.INSTANCE.reservationDTOToEntity(reservationDTO);
			reservationRepository.save(reservationEntity);

			reservationDTO = ModelMapper.INSTANCE.reservationEntityToDTO(reservationEntity);
			return new ResponseEntity<Reservation>(reservationDTO, HttpStatus.OK);

		} catch (DataIntegrityViolationException dve) {
			throw dve;
		} catch (Throwable t) {
			throw t;
		}
	}

	@Override
	public ResponseEntity<Void> updateReservation(Reservation reservationDTO) {
		try {

			ReservationEntity reservationEntity = ModelMapper.INSTANCE.reservationDTOToEntity(reservationDTO);
			reservationRepository.save(reservationEntity);

			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);

		} catch (DataIntegrityViolationException dve) {
			throw dve;
		} catch (Throwable t) {
			throw t;
		}
	}

	@Override
	public ResponseEntity<Reservation> getReservation(Long reservationId) {
		try {

			Reservation reservationDTO = ModelMapper.INSTANCE.reservationEntityToDTO(reservationRepository.findById(reservationId).get());
			return new ResponseEntity<Reservation>(reservationDTO, HttpStatus.OK);

		} catch (NoSuchElementException ne) {
			throw new NoSuchElementException("Could not find Reservation");
		} catch (Exception e) {
			throw e;
		} catch (Throwable t) {
			throw t;
		}
	}

	@Override
	public ResponseEntity<ReservationList> getReservations(Pageable pageable) {
		try {
			List<Reservation> reservationsDTO = new ArrayList<Reservation>();
			reservationRepository.findAll(pageable).forEach(reservation -> reservationsDTO.add(ModelMapper.INSTANCE.reservationEntityToDTO(reservation)));
			ReservationList reservationList = new ReservationList();
			reservationList.setReservations(reservationsDTO);
			return new ResponseEntity<ReservationList>(reservationList, HttpStatus.OK);
		} catch (Throwable t) {
			throw t;
		}
	}

	@Override
	public ResponseEntity<Void> deleteReservation(Long reservationId) {
		try {

			reservationRepository.deleteById(reservationId);
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);

		} catch (NoSuchElementException ne) {
			throw ne;
		} catch (Exception e) {
			throw e;
		} catch (Throwable t) {
			throw t;
		}
	}

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

	@Override
	public ResponseEntity<CustomerList> getCustomers(Pageable pageable) {
		try {
			List<Customer> customersDTO = new ArrayList<Customer>();
			customerRepository.findAll(pageable).forEach(customer -> customersDTO.add(ModelMapper.INSTANCE.customerEntityToDTO(customer)));
			CustomerList customerList = new CustomerList();
			customerList.setCustomers(customersDTO);
			return new ResponseEntity<CustomerList>(customerList, HttpStatus.OK);
		} catch (Throwable t) {
			throw t;
		}
	}

}
