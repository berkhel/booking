package it.berkhel.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.berkhel.booking.app.entity.Attendee;

public interface AttendeeRepository extends JpaRepository<Attendee, String> {

}
