package it.berkhel.booking.drivenadapter;

import org.springframework.stereotype.Repository;

import it.berkhel.booking.app.drivenport.ForStorage;
import it.berkhel.booking.entity.Purchase;
import it.berkhel.booking.repository.AttendeeRepository;
import it.berkhel.booking.repository.PurchaseRepository;
import it.berkhel.booking.repository.TicketRepository;

@Repository
public class StorageXJpaRepository implements ForStorage {

    private final PurchaseRepository purchaseRepo;
    private final TicketRepository ticketRepo;
    private final AttendeeRepository attendeeRepo;

    public StorageXJpaRepository(PurchaseRepository purchaseRepo, TicketRepository ticketRepo, AttendeeRepository attendeeRepo){
        this.purchaseRepo = purchaseRepo;
        this.ticketRepo = ticketRepo;
        this.attendeeRepo = attendeeRepo;
    }


    @Override
    public void save(Purchase purchase) {
        purchaseRepo.save(purchase);
        purchase.getTickets().forEach(ticket -> {
            attendeeRepo.save(ticket.getAttendee());
            ticketRepo.save(ticket);
        });
    }

    @Override
    public Purchase retrieveById(String reservationId) {
        return purchaseRepo.getReferenceById(reservationId);
    }

}
