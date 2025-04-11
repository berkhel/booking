package it.berkhel.booking.dto;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Component;

import it.berkhel.booking.app.entity.Account;
import it.berkhel.booking.app.entity.Attendee;
import it.berkhel.booking.app.entity.Event;
import it.berkhel.booking.app.entity.Purchase;
import it.berkhel.booking.app.entity.TicketEntry;
import it.berkhel.booking.app.exception.BadPurchaseRequestException;
import it.berkhel.booking.app.exception.DuplicateTicketException;
import it.berkhel.booking.app.exception.EventNotFoundException;
import it.berkhel.booking.app.exception.SoldoutException;
import it.berkhel.booking.repository.AccountRepository;
import it.berkhel.booking.repository.EventRepository;

@Component
public class DtoMapper {

    private EventRepository eventRepo;
    private AccountRepository accountRepo;

    public DtoMapper(EventRepository eventRepo, AccountRepository accountRepo) {
        this.eventRepo = eventRepo;
        this.accountRepo = accountRepo;
    }

    public Purchase toObject(PurchaseRequest purchaseRequest) throws BadPurchaseRequestException, EventNotFoundException, DuplicateTicketException, SoldoutException{
        Optional<Account> account = accountRepo.findById(purchaseRequest.accountId);
        Set<TicketEntry> tickets = new HashSet<>();
        for(var dtoTicket : purchaseRequest.tickets){
            try{
                var ticket = toObject(dtoTicket);
                tickets.add(ticket);
            }catch(IllegalArgumentException duplicateEx){
                throw new DuplicateTicketException("Duplicate ticket for attendee " + dtoTicket.getAttendee().id + " and event " + dtoTicket.getEventId());
            }
        }
        if(account.isPresent()){
            return new Purchase(account.get(), tickets);
        }
        return new Purchase(new Account(purchaseRequest.accountId), tickets);

    }

    public TicketEntry toObject(TicketDto ticketDto) throws EventNotFoundException {
        Optional<Event> event = eventRepo.findById(ticketDto.getEventId());
        TicketEntry ticket = new TicketEntry(
                event.orElseThrow(() -> new EventNotFoundException("Event not found : " + ticketDto.getEventId())),
                toObject(ticketDto.getAttendee()));
        return ticket;
    }

    public Event toObject(EventDto eventDto){
        return new Event(eventDto.id, eventDto.maxSeats);
    }

    public Attendee toObject(AttendeeDto attendeeDto){
        return new Attendee(attendeeDto.id, attendeeDto.email, attendeeDto.firstName, attendeeDto.lastName, attendeeDto.birthDate);
    }

    public TicketDto toDto(TicketEntry ticket){
       TicketDto ticketDto = new TicketDto(toDto(ticket.getAttendee()), ticket.getEvent().getId());
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
