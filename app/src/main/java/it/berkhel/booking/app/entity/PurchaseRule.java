package it.berkhel.booking.app.entity;

import java.util.Set;

import it.berkhel.booking.app.exception.BadPurchaseRequestException;

public interface PurchaseRule {

    public void validate(Set<TicketEntry> entries) throws BadPurchaseRequestException;
    
} 
