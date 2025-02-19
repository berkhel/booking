package it.berkhel.booking.dto;

import java.util.Optional;

import org.springframework.stereotype.Component;

import it.berkhel.booking.app.exception.EventNotFoundException;
import it.berkhel.booking.entity.Attendee;
import it.berkhel.booking.entity.Event;
import it.berkhel.booking.entity.Purchase;
import it.berkhel.booking.entity.Ticket;
import it.berkhel.booking.repository.EventRepository;

@Component
public class DtoMapper {

    private EventRepository eventRepo;

    public DtoMapper(EventRepository eventRepo) {
        this.eventRepo = eventRepo;
    }

    public Ticket toObject(TicketDto ticketDto) throws EventNotFoundException {
        Optional<Event> event = eventRepo.findById(ticketDto.getEventId());
        Ticket ticket = new Ticket(
                event.orElseThrow(() -> new EventNotFoundException("Event not found : " + ticketDto.getEventId())),
                toObject(ticketDto.getAttendee()));
        return ticket;
    }

    public Event toObject(EventDto eventDto){
        return new Event(eventDto.id, eventDto.maxSeats, eventDto.remainingSeats);
    }

    public Attendee toObject(AttendeeDto attendeeDto){
        return new Attendee(attendeeDto.id, attendeeDto.email, attendeeDto.firstName, attendeeDto.lastName, attendeeDto.birthDate);
    }

    public TicketDto toDto(Ticket ticket){
       TicketDto ticketDto = new TicketDto(toDto(ticket.getAttendee()), ticket.getEvent().getId());
       ticketDto.setId(ticket.getId());
       return ticketDto;
    }

    public PurchaseDto toDto(Purchase purchase){
        PurchaseDto purchaseDto = new PurchaseDto();
        purchaseDto.setId(purchase.getId());
        purchaseDto.setTickets(purchase.getTickets().stream().map(this::toDto).toList());
        return purchaseDto;
    }

    public AttendeeDto toDto(Attendee attendee){
        var dto = new AttendeeDto();
        dto.id = attendee.getId();
        dto.email = attendee.getEmail();
        dto.firstName = attendee.getFirstName();
        dto.lastName = attendee.getLastName();
        dto.birthDate = attendee.getBirthDate();
        return dto;
    }
    
}
