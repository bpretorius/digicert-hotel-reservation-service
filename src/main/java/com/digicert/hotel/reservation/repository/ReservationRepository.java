package com.digicert.hotel.reservation.repository;

import com.digicert.hotel.reservation.entities.ReservationEntity;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends ListCrudRepository<ReservationEntity, Long>, PagingAndSortingRepository<ReservationEntity, Long> {



}
