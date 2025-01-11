package it.berkhel.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.berkhel.booking.entity.Purchase;

public interface AttendeeRepository extends JpaRepository<Purchase, String> {

}
