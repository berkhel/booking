package it.berkhel.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.berkhel.booking.entity.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, String> {

}
