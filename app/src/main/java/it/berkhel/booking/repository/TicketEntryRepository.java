package it.berkhel.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.berkhel.booking.app.entity.TicketEntry;


public interface TicketEntryRepository extends JpaRepository<TicketEntry, String> {

}
