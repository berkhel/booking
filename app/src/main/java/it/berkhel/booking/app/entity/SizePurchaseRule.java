package it.berkhel.booking.app.entity;

import java.util.Set;

import it.berkhel.booking.app.exception.BadPurchaseRequestException;

public class SizePurchaseRule implements PurchaseRule{
    

    public void validate(Set<TicketEntry> entries) throws BadPurchaseRequestException{
        validateSize(entries);
    }
    
    private void validateSize(Set<TicketEntry> ticketEntries) throws BadPurchaseRequestException {
        if(ticketEntries.size() < 1){
            throw new BadPurchaseRequestException("At least one ticket must be included in the request");
        }
        if(ticketEntries.size() > 3){
            throw new BadPurchaseRequestException("Cannot purchase more than 3 tickets");
        }
    }
}
