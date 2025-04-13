package it.berkhel.booking.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.berkhel.booking.app.actionport.ForBooking;
import it.berkhel.booking.app.entity.Purchase;
import it.berkhel.booking.app.exception.BadPurchaseRequestException;
import it.berkhel.booking.app.exception.ConcurrentPurchaseException;
import it.berkhel.booking.app.exception.DuplicateTicketException;
import it.berkhel.booking.app.exception.EventNotFoundException;
import it.berkhel.booking.app.exception.SoldoutException;

@Transactional
@Service
public class TransactionalBookingService {

    private ForBooking bookingManager;

    public TransactionalBookingService(ForBooking bookingManager){
        this.bookingManager = bookingManager;
    }

    public void process(Purchase purchase) throws BadPurchaseRequestException, EventNotFoundException, DuplicateTicketException, SoldoutException, ConcurrentPurchaseException {
        bookingManager.process(purchase);
    }
}
