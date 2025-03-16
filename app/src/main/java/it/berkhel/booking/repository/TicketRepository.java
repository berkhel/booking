package it.berkhel.booking.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import it.berkhel.booking.app.entity.TicketEntry;


public interface TicketRepository extends JpaRepository<TicketEntry, String> {

    Optional<TicketEntry> findByEventIdAndAttendeeId(String eventId, String attendeeId);
}
