package it.berkhel.booking.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.berkhel.booking.entity.Ticket;
import it.berkhel.booking.repository.EventRepository;

@Component
public class DtoMapper {

    private EventRepository eventRepo;

    public DtoMapper(EventRepository eventRepo) {
        this.eventRepo = eventRepo;
    }

    public Ticket toObject(TicketDto ticketDto){
        Ticket ticket = new Ticket();
        ticket.setAttendee(ticketDto.getAttendee());
        ticket.setEvent(eventRepo.getReferenceById(ticketDto.getEventId()));
        return ticket;
    }

    
}
