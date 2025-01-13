package it.berkhel.booking.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import it.berkhel.booking.entity.Ticket;


public interface TicketRepository extends JpaRepository<Ticket, String> {

    Optional<Ticket> findByEventIdAndAttendeeId(String eventId, String attendeeId);
}
