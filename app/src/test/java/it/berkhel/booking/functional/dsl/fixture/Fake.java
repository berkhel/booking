package it.berkhel.booking.functional.dsl.fixture;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Fake {
    

    public static final String singlePurchaseForEvent(String eventId){ 
       return "{ \"accountId\": \"DEFAULT\", "+
       "\"tickets\": [{ \"eventId\":\""+eventId+ "\","+
       " \"attendee\" :"+
       " {\"id\" : \"ABCD0001\","+
       " \"email\":\"mario.rossi@example.it\","+
       " \"firstName\":\"Mario\","+
       " \"lastName\": \"Rossi\","+
       " \"birthDate\":\"1990-01-01\"}}]}" ;}
    
    

    public static class Purchase {
        private List<Ticket> tickets = new ArrayList<>();

        private Purchase(){}

        public static Purchase json(){
            return new Purchase();
        }

        public Purchase with2RandomTickets(){
            tickets.add(Fake.Ticket.json().random());
            tickets.add(Fake.Ticket.json().random());
            return this;
        }

        public Purchase withTicket(Ticket ticket){
            this.tickets.add(ticket);
            return this;
        }

        public String build(){
            return "[" + tickets.stream()
                    .map(Ticket::build)
                    .collect(Collectors.joining(",")) + "]";

        }

        
    }
    public static class PurchaseRequest {
        private List<Ticket> tickets = new ArrayList<>();
        private String accountId = "DEFAULT";

        private PurchaseRequest(){}

        public static PurchaseRequest json(){
            return new PurchaseRequest();
        }

        public PurchaseRequest withTicket(Ticket ticket){
            this.tickets.add(ticket);
            return this;
        }

        public String build(){
            return "{ \"accountId\":\""+accountId+"\" ,\"tickets\": [" + tickets.stream()
                    .map(Ticket::build)
                    .collect(Collectors.joining(",")) + "]}";

        }

        
    }

    public static class Ticket{

        private String eventId;
        private Attendee attendee;

        private Ticket() {
        }

        public static Ticket json() {
            return new Ticket();
        }

        public Ticket random(){
            this.eventId = UUID.randomUUID().toString();
            this.attendee = Attendee.json().random();
            return this;
        }

        public Ticket withEvent(String eventId) {
            this.eventId = eventId;
            return this;
        }

        public Ticket withAttendee(Attendee attendee) {
            this.attendee = attendee;
            return this;
        }

        public String build(){
            return "{ \"eventId\": \"" + eventId + "\", \"attendee\":" + attendee.build() + "}";
        }


    }

    public static class Attendee{
        private String id;

        private Attendee(){

        }
        public static Attendee json(){
            return new Attendee();
        }

        public Attendee random(){
            this.id = UUID.randomUUID().toString();
            return this;
        }

        public Attendee withId(String id){
            this.id = id;
            return this;
        }

        public String build() {
            return "{ \"id\": \"" + id + "\", " +
                    " \"email\":\"mario.rossi@example.it\"," +
                    " \"firstName\":\"Mario\"," +
                    " \"lastName\": \"Rossi\"," +
                    " \"birthDate\":\"1990-01-01\"}";
        }
    }
}