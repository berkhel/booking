package it.berkhel.booking;

import java.util.UUID;

public class App implements ForBooking {

    public static App init(ForStorage storage){
        return new App(storage);
    }
    
    private ForStorage storage;

    private App(ForStorage storage) {
        this.storage = storage;
    }

    @Override
    public String book(){
        storage.storeBooking();
        return UUID.randomUUID().toString();
    }


}