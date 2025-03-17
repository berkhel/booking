package it.berkhel.booking.drivenadapter;

import java.util.Optional;

import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import it.berkhel.booking.app.drivenport.ForStorage;
import it.berkhel.booking.app.entity.Event;
import it.berkhel.booking.app.entity.Purchase;
import it.berkhel.booking.app.entity.TicketEntry;
import it.berkhel.booking.app.exception.ConcurrentPurchaseException;
import it.berkhel.booking.repository.AttendeeRepository;
import it.berkhel.booking.repository.EventRepository;
import it.berkhel.booking.repository.PurchaseRepository;
import it.berkhel.booking.repository.TicketRepository;
import it.berkhel.booking.repository.TicketEntryRepository;

@Repository
@Transactional
public class JpaStorage implements ForStorage {

    private final PurchaseRepository purchaseRepo;
    private final TicketEntryRepository ticketEntryRepo;
    private final AttendeeRepository attendeeRepo;
    private final EventRepository eventRepo;

    public JpaStorage(PurchaseRepository purchaseRepo, TicketEntryRepository ticketEntryRepo,
            AttendeeRepository attendeeRepo, EventRepository eventRepo) {
        this.purchaseRepo = purchaseRepo;
        this.ticketEntryRepo = ticketEntryRepo;
        this.attendeeRepo = attendeeRepo;
        this.eventRepo = eventRepo;
    }

    @Override
    public Purchase save(Purchase purchase) throws ConcurrentPurchaseException {
        purchaseRepo.save(purchase);
        for (var ticket : purchase.getTickets()) {
            try {
                eventRepo.saveAndFlush(ticket.getEvent());
            } catch (ObjectOptimisticLockingFailureException ex) {
                throw new ConcurrentPurchaseException("Cannot purchase tickets concurrently");
            }
            attendeeRepo.saveAndFlush(ticket.getAttendee());
            ticketEntryRepo.save(ticket);
        }
        return purchase;
    }


    @Override
    public Event save(Event event) {
        return eventRepo.save(event);
    }

    @Override
    public Optional<Event> getEventById(String eventId) {
        return eventRepo.findById(eventId);
    }

}
