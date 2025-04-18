package it.berkhel.booking.drivenadapter;

import java.util.Optional;

import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Repository;
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
        Account account = purchase.getAccount();
        accountRepo.saveAndFlush(account); //can be new or old
        purchaseRepo.save(purchase); //always new
        for (TicketEntry ticketEntry : purchase.getTicketEntries()) {
            attendeeRepo.saveAndFlush(ticketEntry.getAttendee()); //can be new or old, but unmanaged
            Event event = ticketEntry.getTicket().getEvent();
            eventRepo.save(event); //always old
            ticketRepo.save(ticketEntry.getTicket()); //always old
            ticketEntryRepo.save(ticketEntry); //always new
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
