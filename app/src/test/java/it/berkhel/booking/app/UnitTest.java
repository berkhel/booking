package it.berkhel.booking.app;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import it.berkhel.booking.app.App;
import it.berkhel.booking.app.actionport.ForBooking;
import it.berkhel.booking.app.drivenport.ForSendingMessage;
import it.berkhel.booking.app.drivenport.ForStorage;
import it.berkhel.booking.app.entity.Account;
import it.berkhel.booking.app.entity.Attendee;
import it.berkhel.booking.app.entity.Event;
import it.berkhel.booking.app.entity.Purchase;
import it.berkhel.booking.app.entity.TicketEntry;
import it.berkhel.booking.app.exception.BadPurchaseRequestException;
import it.berkhel.booking.app.exception.DuplicateTicketException;
import it.berkhel.booking.app.exception.EventAlreadyExistsException;
import it.berkhel.booking.app.exception.EventNotFoundException;
import it.berkhel.booking.app.exception.SoldoutException;
import it.berkhel.booking.app.fixture.Fake;
import it.berkhel.booking.dto.PurchaseRequest;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Optional;
import java.util.Set;
import static java.util.stream.Collectors.toSet;
import java.util.stream.Stream;


@ExtendWith(MockitoExtension.class)
class UnitTest {

    @BeforeEach
    void restoreApp(){
        App.instance = null;
    }

    @Test void booking_targets_the_storage(@Mock ForStorage theStorage, @Mock ForSendingMessage messageBroker) throws Exception {
        when(theStorage.getEventById(Mockito.anyString())).thenReturn(Optional.of(new Event("EV0000", 1)));
        when(theStorage.getAccountById(Mockito.anyString())).thenReturn(Optional.of(new Account("TEST")));
        ForBooking app = App.init(theStorage, messageBroker);

        app.purchase(new Purchase("test", Set.of(Fake.ticket())), "TEST");

        verify(theStorage).save(Mockito.any(Purchase.class));
    }

    // @Test void booking_targets_the_message_broker(@Mock ForStorage aStorage, @Mock ForSendingMessage theMessageBroker) throws Exception {
    //     when(aStorage.getEventById(Mockito.anyString())).thenReturn(Optional.of(new Event("EV0000", 1)));
    //     when(aStorage.getAccountById(Mockito.anyString())).thenReturn(Optional.of(new Account("TEST")));
    //     ForBooking app = App.init(aStorage, theMessageBroker);

    //     app.purchase(new Purchase("test" ,Set.of(Fake.ticket())), "TEST");

    //     verify(theMessageBroker).sendMessage(Mockito.any(), Mockito.any());
    // }

    @Test void booking_normally_return_a_response(@Mock ForStorage aStorage, @Mock ForSendingMessage aMessageBroker) throws Exception {
        when(aStorage.getEventById(Mockito.anyString())).thenReturn(Optional.of(new Event("EV0000", 1)));
        when(aStorage.getAccountById(Mockito.anyString())).thenReturn(Optional.of(new Account("TEST")));
        ForBooking app = App.init(aStorage, aMessageBroker);

        Purchase thePurchase = app.purchase(new Purchase("test", Set.of(Fake.ticket())), "TEST");

        assertThat(thePurchase, any(Purchase.class));
    }

    @Test
    void not_more_than_three_tickets_for_purchase(@Mock ForStorage aStorage, @Mock ForSendingMessage aMessageBroker){
        Set<TicketEntry> fourTickets = Stream.generate(Fake::ticket).limit(4).collect(toSet());
        ForBooking app = App.init(aStorage, aMessageBroker);

        assertThrows(BadPurchaseRequestException.class, () -> {
            app.purchase(new Purchase("test",fourTickets), "TEST");
        });
    }

    @Test
    void three_tickets_for_purchase_are_allowed(@Mock ForStorage aStorage, @Mock ForSendingMessage aMessageBroker){
        when(aStorage.getEventById(Mockito.anyString())).thenReturn(Optional.of(new Event("EV0000", 10)));
        when(aStorage.getAccountById(Mockito.anyString())).thenReturn(Optional.of(new Account("TEST")));
        Set<TicketEntry> threeTickets = Stream.generate(Fake::ticket).limit(3).collect(toSet());
        ForBooking app = App.init(aStorage, aMessageBroker);

        assertDoesNotThrow(() -> {
            app.purchase(new Purchase("test",threeTickets), "TEST");
        });

    }

    @Test
    void zero_tickets_for_purchase_are_not_allowed(@Mock ForStorage aStorage, @Mock ForSendingMessage aMessageBroker){
        Set<TicketEntry> noTickets = Set.of();
        ForBooking app = App.init(aStorage, aMessageBroker);

        assertThrows(BadPurchaseRequestException.class, () -> {
            app.purchase(new Purchase("test",noTickets), "TEST");
        });

    }

    @Test
    void a_null_list_of_tickets_for_purchase_is_not_allowed(@Mock ForStorage aStorage, @Mock ForSendingMessage aMessageBroker){
        Set<TicketEntry> nullTickets = null;
        ForBooking app = App.init(aStorage, aMessageBroker);

        assertThrows(Exception.class, () -> {
            app.purchase(new Purchase("test",nullTickets), "TEST");
        });

    }

    @Test
    void cannot_purchase_after_soldout(@Mock ForStorage aStorage, @Mock ForSendingMessage aMessageBroker) throws Exception{
        when(aStorage.getEventById("EVSLDOUT1")).thenReturn(Optional.of(new Event("EVSLDOUT1", 0)));
        when(aStorage.getAccountById(Mockito.anyString())).thenReturn(Optional.of(new Account("TEST")));
        TicketEntry arrivedLate = new TicketEntry("EVSLDOUT1", Fake.attendee());

        ForBooking app = App.init(aStorage, aMessageBroker);

        assertThrows(SoldoutException.class, () -> {
            app.purchase(new Purchase("test", Set.of(arrivedLate)), "TEST");
        });
    }

    @Test
    void new_ticket_should_decrease_available_seats_by_one(@Mock ForStorage aStorage, @Mock ForSendingMessage aMessageBroker) throws Exception{
        final String eventId = "EV0001";
        Event event = new Event(eventId, 10);
        when(aStorage.getEventById(eventId)).thenReturn(Optional.of(event));
        when(aStorage.getAccountById(Mockito.anyString())).thenReturn(Optional.of(new Account("TEST")));

        TicketEntry newTicket = new TicketEntry(eventId, Fake.attendee());

        App app = App.init(aStorage, aMessageBroker);

        app.purchase(new Purchase("test",Set.of(newTicket)), "TEST");

        assertEquals(9, event.getRemainingSeats());

    }

    @Test
    void event_cannot_be_empty(@Mock ForStorage aStorage, @Mock ForSendingMessage aMessageBroker) throws Exception{
        assertThrows(EventNotFoundException.class, () -> {
            new TicketEntry(null, Fake.attendee());
        });
    }


    @Test
    void can_detect_duplicate_events(@Mock ForStorage theStorage, @Mock ForSendingMessage aMessageBroker) throws Exception{
        when(theStorage.getEventById(Mockito.anyString())).thenReturn(Optional.of(Fake.event()));

        App app = App.init(theStorage, aMessageBroker);

        assertThrows(EventAlreadyExistsException.class, () -> {
            app.createEvent(Fake.event());
        });

    }

    @Test
    void a_purchase_cannot_contains_the_same_ticket_twice(@Mock ForStorage aStorage, @Mock ForSendingMessage aMessageBroker) throws Exception{
        final String eventId = "0001";

        TicketEntry ticketA = new TicketEntry(eventId, new Attendee("AB01", "/", "/", "/", "/"));
        TicketEntry ticketB = new TicketEntry(eventId, new Attendee("AB01", "/", "/", "/", "/"));

        App app = App.init(aStorage, aMessageBroker);

        assertThrows(RuntimeException.class, () -> {
            app.purchase(new Purchase("test",Set.of(ticketA, ticketB)), "TEST");
        });



    }

}
