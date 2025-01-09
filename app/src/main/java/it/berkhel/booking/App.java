package it.berkhel.booking;


public class App implements ForBooking {

    public static App init(ForStorage storage){
        return new App(storage);
    }
    
    private ForStorage storage;

    private App(ForStorage storage) {
        this.storage = storage;
    }

    @Override
    public String book(Reservation aReservation){
        storage.save(aReservation);
        return aReservation.getId();
    }


}