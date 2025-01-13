package it.berkhel.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.berkhel.booking.entity.Attendee;
import it.berkhel.booking.entity.Event;
import it.berkhel.booking.entity.Ticket;


public interface TicketRepository extends JpaRepository<Ticket, String> {

    Ticket findByEventIdAndAttendeeId(String eventId, String attendeeId);
}
