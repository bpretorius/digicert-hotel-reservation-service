package com.hotel.reservation.repository;

import com.hotel.reservation.entities.HotelEntity;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelRepository extends ListCrudRepository<HotelEntity, Long>, PagingAndSortingRepository<HotelEntity, Long> {



}
