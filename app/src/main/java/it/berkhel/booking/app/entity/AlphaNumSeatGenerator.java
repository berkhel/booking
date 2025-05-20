package it.berkhel.booking.app.entity;

import java.util.function.Supplier;

public class AlphaNumSeatGenerator implements Supplier<String> {

    private Character lastTicketLetter = 'A';
    private Integer lastTicketNumber = 0;


    @Override
    public String get() {
        if ((lastTicketLetter + lastTicketNumber) % 2 == 0) {
            lastTicketNumber++;
            if (lastTicketLetter > 'A') {
                lastTicketLetter--;
            }
        } else {
            lastTicketLetter++;
            if (lastTicketNumber > 0) {
                lastTicketNumber--;
            }
        }
        return lastTicketLetter + "" + lastTicketNumber;
    }
    
}
