package it.berkhel.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.berkhel.booking.entity.Attendee;

public interface AttendeeRepository extends JpaRepository<Attendee, String> {

}
