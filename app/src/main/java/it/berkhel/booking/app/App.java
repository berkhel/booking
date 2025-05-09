package it.berkhel.booking.app;


import it.berkhel.booking.app.actionport.ForBooking;
import it.berkhel.booking.app.actionport.ForEvents;
import it.berkhel.booking.app.drivenport.ForSendingMessage;
import it.berkhel.booking.app.drivenport.ForStorage;
import it.berkhel.booking.app.entity.Event;
import it.berkhel.booking.app.entity.Purchase;
import it.berkhel.booking.app.exception.BadPurchaseRequestException;
import it.berkhel.booking.app.exception.ConcurrentPurchaseException;
import it.berkhel.booking.app.exception.DuplicateTicketException;
import it.berkhel.booking.app.exception.EventAlreadyExistsException;
import it.berkhel.booking.app.exception.EventNotFoundException;
import it.berkhel.booking.app.exception.SoldoutException;

public class App implements ForBooking, ForEvents {

    static App instance;

    public static App init(ForStorage storage, ForSendingMessage messageSender){
        if(instance != null){
            throw new RuntimeException("Cannot instanciate the app twice");
        }
        instance = new App(storage, messageSender);
        return instance;
    }
    
    private final PurchaseApp purchaseApp;
    private final EventApp eventApp;

    private App(ForStorage storage, ForSendingMessage messageSender) {
        this.purchaseApp = new PurchaseApp(storage, messageSender);
        this.eventApp = new EventApp(storage);
    }

    @Override
    public Purchase process(Purchase purchase) throws BadPurchaseRequestException, EventNotFoundException, DuplicateTicketException, SoldoutException, ConcurrentPurchaseException {
        purchaseApp.process(purchase);
        return purchase;
    }

    @Override
    public Event createEvent(Event event) throws EventAlreadyExistsException {
        return eventApp.createEvent(event);
    }


}