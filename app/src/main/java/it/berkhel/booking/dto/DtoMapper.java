package it.berkhel.booking.dto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import it.berkhel.booking.app.entity.AlphaNumSeatGenerator;
import it.berkhel.booking.app.entity.Attendee;
import it.berkhel.booking.app.entity.Event;
import it.berkhel.booking.app.entity.Purchase;
import it.berkhel.booking.app.entity.SizePurchaseRule;
import it.berkhel.booking.app.entity.TicketEntry;
import it.berkhel.booking.app.exception.BadPurchaseRequestException;
import it.berkhel.booking.app.exception.DuplicateTicketException;
import it.berkhel.booking.app.exception.EventNotFoundException;
import it.berkhel.booking.app.exception.SoldoutException;

@Component
public class DtoMapper {



    public Purchase toObject(PurchaseRequest purchaseRequest) throws BadPurchaseRequestException, EventNotFoundException, DuplicateTicketException, SoldoutException{
        Set<TicketEntry> tickets = new HashSet<>();
        for(var dtoTicket : purchaseRequest.tickets){
            try{
                var ticket = toObject(dtoTicket);
                tickets.add(ticket);
            }catch(IllegalArgumentException duplicateEx){
                throw new DuplicateTicketException("Duplicate ticket for attendee " + dtoTicket.getAttendee().id + " and event " + dtoTicket.getEventId());
            }
        }
        return new Purchase(purchaseRequest.accountId, tickets, List.of(new SizePurchaseRule()));

    }

    public TicketEntry toObject(TicketDto ticketDto) throws EventNotFoundException {
        TicketEntry ticket = new TicketEntry(
                ticketDto.getEventId(),
                toObject(ticketDto.getAttendee()));
        return ticket;
    }

    public Event toObject(EventDto eventDto){
        return new Event(eventDto.id, eventDto.maxSeats);
    }

    public Attendee toObject(AttendeeDto attendeeDto){
        return Attendee.createAttendee(attendeeDto.id, attendeeDto.email, attendeeDto.firstName, attendeeDto.lastName, attendeeDto.birthDate);
    }

    public TicketDto toDto(TicketEntry ticket){
       TicketDto ticketDto = new TicketDto(toDto(ticket.getAttendee()), ticket.getEventId());
       ticketDto.setId(ticket.getId());
       return ticketDto;
    }

    public PurchaseDto toDto(Purchase purchase){
        PurchaseDto purchaseDto = new PurchaseDto();
        purchaseDto.setId(purchase.getId());
        purchaseDto.setTickets(purchase.getTicketEntries().stream().map(this::toDto).toList());
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
