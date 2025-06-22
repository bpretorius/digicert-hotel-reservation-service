package com.hotel.reservation;

import com.hotel.reservation.api.model.Customer;
import com.hotel.reservation.api.model.Hotel;
import com.hotel.reservation.api.model.Reservation;
import com.hotel.reservation.entities.CustomerEntity;
import com.hotel.reservation.entities.HotelEntity;
import com.hotel.reservation.repository.CustomerRepository;
import com.hotel.reservation.repository.HotelRepository;
import com.hotel.reservation.repository.ReservationRepository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Before;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;

import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.MOCK,
		classes = ReservationApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(
		locations = "classpath:application-test.yml")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ReservationApplicationTest {

	@Autowired
	private MockMvc mvc;
	@Autowired
	HotelRepository hotelRepository;
	@Autowired
	CustomerRepository customerRepository;
	@Autowired
	ReservationRepository reservationRepository;

	@Before
	public void setUp() {

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
	@Test
	@Order(1)
	public void createReservation200() throws Exception {

		Reservation reservation = new Reservation();
		Customer customer = new Customer();
		customer.setId(1L);
		reservation.setCustomer(customer);
		Hotel hotel = new Hotel();
		hotel.setId(1L);
		reservation.setHotel(hotel);
		reservation.setNumberOfAdults(2);
		reservation.setNumberOfChildren(2);
		reservation.setFromDate("2023-07-01T00:00:00.000Z");
		reservation.setToDate("2023-07-01T00:00:00.000Z");
		reservation.setReservationReference("Refxxxxxxxx");

		mvc.perform(post("/reservation").content(asJsonString(reservation))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

	}


	@Test
	@Order(2)
	public void getReservation200() throws Exception {

		mvc.perform(get("/reservation/1")
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1));
	}

	@Test
	@Order(3)
	public void getReservations200() throws Exception {

		mvc.perform(get("/reservation/list")
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

	}

	@Test
	@Order(4)
	public void updateReservation204() throws Exception {

		Reservation reservation = new Reservation();
		reservation.setId(1L);
		Customer customer = new Customer();
		customer.setId(1L);
		reservation.setCustomer(customer);
		Hotel hotel = new Hotel();
		hotel.setId(1L);
		reservation.setHotel(hotel);
		reservation.setFromDate("2023-07-01T00:00:00.000Z");
		reservation.setToDate("2023-07-01T00:00:00.000Z");
		reservation.setReservationReference("Refxxxxxxxx");
		reservation.setNumberOfAdults(2);
		reservation.setNumberOfChildren(2);

		mvc.perform(put("/reservation").content(asJsonString(reservation))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());

	}

	@Test
	@Order(5)
	public void deleteReservation204() throws Exception {

		mvc.perform(delete("/reservation/1")
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());
	}

	public static String asJsonString(final Object obj) {
		try {
			final ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());
			final String jsonContent = mapper.writeValueAsString(obj);
			return jsonContent;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
