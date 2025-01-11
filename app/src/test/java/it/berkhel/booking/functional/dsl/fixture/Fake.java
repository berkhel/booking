package it.berkhel.booking.functional.dsl.fixture;

public class Fake {
    

    public static final String singlePurchaseForEvent(String eventId){ 
       return "[{ \"eventId\":\""+eventId+ "\","+
       " \"attendee\" :"+
       " {\"id\" : \"ABCD0001\","+
       " \"firstName\":\"Mario\","+
       " \"lastName\": \"Rossi\","+
       " \"birthDate\":\"1990-01-01\"}}]" ;}

}