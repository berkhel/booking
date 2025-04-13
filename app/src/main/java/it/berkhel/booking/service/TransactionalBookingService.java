package it.berkhel.booking.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.berkhel.booking.app.actionport.ForBooking;
import it.berkhel.booking.app.entity.Purchase;

@Transactional
@Service
public class TransactionalBookingService {

    private ForBooking bookingManager;

    public TransactionalBookingService(ForBooking bookingManager){
        this.bookingManager = bookingManager;
    }

    public void process(Purchase purchase) throws Exception{
        bookingManager.process(purchase);
    }
}
