package it.berkhel.booking;

import java.util.UUID;

public class App implements ForBooking {

    public static App init(ForStorage storage){
        return new App(storage);
    }
    
    private ForStorage storage;
    private String id;

    private App(ForStorage storage) {
        this.id = UUID.randomUUID().toString();
        this.storage = storage;
    }

    @Override
    public String book(){
        storage.storeBooking();
        return this.id;
    }


}