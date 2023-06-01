package com.digicert.hotel.reservation.controller;

import com.digicert.hotel.reservation.api.CustomerApiDelegate;
import com.digicert.hotel.reservation.api.model.Customer;
import com.digicert.hotel.reservation.api.model.CustomerList;
import com.digicert.hotel.reservation.mappers.ModelMapper;
import com.digicert.hotel.reservation.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerApiDelegateImpl implements CustomerApiDelegate {

	@Autowired
	CustomerRepository customerRepository;

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
