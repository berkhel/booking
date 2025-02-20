package it.berkhel.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.berkhel.booking.app.entity.Event;


public interface EventRepository extends JpaRepository<Event, String> {
    
}
