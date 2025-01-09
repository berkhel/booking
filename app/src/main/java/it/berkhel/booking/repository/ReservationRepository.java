package it.berkhel.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.berkhel.booking.entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, String> {

}
