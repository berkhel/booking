package it.berkhel.booking.dto;

import org.springframework.stereotype.Component;

import it.berkhel.booking.entity.Purchase;
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

    public TicketDto toDto(Ticket ticket){
       TicketDto ticketDto = new TicketDto(ticket.getAttendee(), ticket.getEvent().getId());
       ticketDto.setId(ticket.getId());
       return ticketDto;
    }

    public PurchaseDto toDto(Purchase purchase){
        PurchaseDto purchaseDto = new PurchaseDto();
        purchaseDto.setId(purchase.getId());
        purchaseDto.setTickets(purchase.getTickets().stream().map(this::toDto).toList());
        return purchaseDto;
    }

    
}
