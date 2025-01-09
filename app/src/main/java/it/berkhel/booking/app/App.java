package it.berkhel.booking.app;

import it.berkhel.booking.app.actionport.ForBooking;
import it.berkhel.booking.app.drivenport.ForStorage;
import it.berkhel.booking.entity.Purchase;

public class App implements ForBooking {

    public static App init(ForStorage storage){
        return new App(storage);
    }
    
    private ForStorage storage;

    private App(ForStorage storage) {
        this.storage = storage;
    }

    @Override
    public Purchase book(){
        Purchase reservation = new Purchase();
        storage.save(reservation);
        return reservation;
    }


}