package it.berkhel.booking;

import it.berkhel.booking.MainConfig;

public class App implements ForBooking {

    public static App init(ForStorage storage){
        return new App(storage);
    }
    
    private ForStorage storage;

    private App(ForStorage storage) {
        this.storage = storage;
    }

    @Override
    public void book(){
        storage.storeBooking();
    }


}