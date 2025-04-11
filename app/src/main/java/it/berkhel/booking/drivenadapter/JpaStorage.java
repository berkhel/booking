package it.berkhel.booking.drivenadapter;

import java.util.Optional;

import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import it.berkhel.booking.app.drivenport.ForStorage;
import it.berkhel.booking.app.entity.Account;
import it.berkhel.booking.app.entity.Event;
import it.berkhel.booking.app.entity.Purchase;
import it.berkhel.booking.app.entity.TicketEntry;
import it.berkhel.booking.app.exception.ConcurrentPurchaseException;
import it.berkhel.booking.repository.AccountRepository;
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
    private final AccountRepository accountRepo;
    private final TicketRepository ticketRepo;

    public JpaStorage(PurchaseRepository purchaseRepo, TicketEntryRepository ticketEntryRepo,
            AttendeeRepository attendeeRepo, EventRepository eventRepo, AccountRepository accountRepo, TicketRepository ticketRepo) {
        this.purchaseRepo = purchaseRepo;
        this.ticketEntryRepo = ticketEntryRepo;
        this.attendeeRepo = attendeeRepo;
        this.eventRepo = eventRepo;
        this.accountRepo = accountRepo;
        this.ticketRepo = ticketRepo;
    }

    @Override
    public Purchase save(Purchase purchase) throws ConcurrentPurchaseException {
        // Account account = purchase.getAccount();
        // accountRepo.saveAndFlush(account);
        // for (var ticketEntry : purchase.getTicketEntries()) {
        // var ticket = ticketEntry.getTicket();
        // attendeeRepo.saveAndFlush(ticket.getAttendee());
        // try {
        // eventRepo.saveAndFlush(ticketEntry.getTicket().getEvent());
        // } catch (ObjectOptimisticLockingFailureException ex) {
        // throw new ConcurrentPurchaseException("Cannot purchase tickets
        // concurrently");
        // }
        // purchaseRepo.save(purchase);
        // ticketEntryRepo.saveAndFlush(ticketEntry);
        // }

        // 1. Save Account first
        Account account = purchase.getAccount();
        accountRepo.saveAndFlush(account);

        // 2. Save all Attendees
        for (TicketEntry entry : purchase.getTicketEntries()) {
            attendeeRepo.saveAndFlush(entry.getAttendee());
        }

        // 3. Save Purchase (now that Account is saved)
        purchaseRepo.saveAndFlush(purchase);

        // 4. Process each ticket entry and associated Event/Ticket
        for (TicketEntry ticketEntry : purchase.getTicketEntries()) {
            // 5. Save/update Event (with optimistic locking)
            Event event = ticketEntry.getTicket().getEvent();
            try {
                eventRepo.saveAndFlush(event);
            } catch (ObjectOptimisticLockingFailureException ex) {
                throw new ConcurrentPurchaseException("Cannot purchase tickets concurrently");
            }

            // 6. Save Ticket if needed (might be handled by cascade)
            if (ticketRepo != null) {
                ticketRepo.saveAndFlush(ticketEntry.getTicket());
            }

            // 7. Finally save TicketEntry
            ticketEntryRepo.saveAndFlush(ticketEntry);
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

    @Override
    public Optional<Account> getAccountById(String accountId) {
        return accountRepo.findById(accountId);
    }

    @Override
    public Account saveAccount(Account account) {
        return accountRepo.saveAndFlush(account);
    }
}
