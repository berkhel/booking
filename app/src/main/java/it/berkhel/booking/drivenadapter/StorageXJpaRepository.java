package it.berkhel.booking.drivenadapter;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import it.berkhel.booking.app.drivenport.ForStorage;
import it.berkhel.booking.app.exception.TransactionPostConditionException;
import it.berkhel.booking.entity.Attendee;
import it.berkhel.booking.entity.Event;
import it.berkhel.booking.entity.Purchase;
import it.berkhel.booking.entity.Ticket;
import it.berkhel.booking.repository.AttendeeRepository;
import it.berkhel.booking.repository.EventRepository;
import it.berkhel.booking.repository.PurchaseRepository;
import it.berkhel.booking.repository.TicketRepository;

@Repository
public class StorageXJpaRepository implements ForStorage {


    private final PurchaseRepository purchaseRepo;
    private final TicketRepository ticketRepo;
    private final AttendeeRepository attendeeRepo;
    private final EventRepository eventRepo;

    public StorageXJpaRepository(PurchaseRepository purchaseRepo, TicketRepository ticketRepo,
            AttendeeRepository attendeeRepo, EventRepository eventRepo) {
        this.purchaseRepo = purchaseRepo;
        this.ticketRepo = ticketRepo;
        this.attendeeRepo = attendeeRepo;
        this.eventRepo = eventRepo;
    }

    @Transactional
    @Override
    public void save(Purchase purchase, Predicate<Purchase> postCondition) throws TransactionPostConditionException {
        purchaseRepo.save(purchase);
        for(var ticket : purchase.getTickets()){
            attendeeRepo.saveAndFlush(ticket.getAttendee());
            ticketRepo.save(ticket);
        }

        if (postCondition.negate().test(purchase)) {
            throw new TransactionPostConditionException();
        }
    }


    @Override
    public Event save(Event event) {
        return eventRepo.save(event);
    }

    @Override
    public Optional<Event> getEventById(String eventId) {
        return eventRepo.findById(eventId);
    }

    @Override
    public Optional<Ticket> getTicketBy(String eventId, String attendeeId){

        return ticketRepo.findByEventIdAndAttendeeId(eventId, attendeeId);

    }
}
