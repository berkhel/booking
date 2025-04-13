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
import java.util.Random;
import java.util.Set;
import static java.util.stream.Collectors.toSet;
import java.util.stream.Stream;


@ExtendWith(MockitoExtension.class)
class UnitTest {

    @BeforeEach
    void restoreApp(){
        App.instance = null;
        Attendee.registry.clear();
    }

    @Test 
    void booking_targets_the_storage(@Mock ForStorage theStorage, @Mock ForSendingMessage messageBroker) throws Exception {
        App app = App.init(theStorage, messageBroker);

        app.purchase(Fake.purchase(Fake.account(), Set.of(Fake.ticket(Fake.event(), Fake.attendee()))));

        verify(theStorage).save(Mockito.any(Purchase.class));
    }

    // @Test void booking_targets_the_message_broker(@Mock ForStorage aStorage, @Mock ForSendingMessage theMessageBroker) throws Exception {
    //     when(aStorage.getEventById(Mockito.anyString())).thenReturn(Optional.of(new Event("EV0000", 1)));
    //     when(aStorage.getAccountById(Mockito.anyString())).thenReturn(Optional.of(new Account("TEST")));
    //     ForBooking app = App.init(aStorage, theMessageBroker);

    //     app.purchase(new Purchase("test" ,Set.of(Fake.ticket())), "TEST");

    //     verify(theMessageBroker).sendMessage(Mockito.any(), Mockito.any());
    // }

    @Test 
    void booking_normally_return_a_response(@Mock ForStorage aStorage, @Mock ForSendingMessage aMessageBroker) throws Exception {
        App app = App.init(aStorage, aMessageBroker);

        Purchase thePurchase = app.purchase(Fake.purchase(Fake.account(), Set.of(Fake.ticket(Fake.event(), Fake.attendee()))));

        assertThat(thePurchase, any(Purchase.class));
    }

    @Test
    void not_more_than_three_tickets_for_purchase(@Mock ForStorage aStorage, @Mock ForSendingMessage aMessageBroker){
        Set<TicketEntry> fourTickets = Stream.generate(() -> Fake.ticket(Fake.event(), Fake.attendee())).limit(4).collect(toSet());
        App app = App.init(aStorage, aMessageBroker);

        assertThrows(BadPurchaseRequestException.class, () -> {
            app.purchase(Fake.purchase(Fake.account(), fourTickets));
        });
    }

    @Test
    void three_tickets_for_purchase_are_allowed(@Mock ForStorage aStorage, @Mock ForSendingMessage aMessageBroker){
        Set<TicketEntry> threeTickets = Stream.generate(() -> Fake.ticket(Fake.event(), Fake.attendee())).limit(3).collect(toSet());
        App app = App.init(aStorage, aMessageBroker);

        assertDoesNotThrow(() -> {
            app.purchase(Fake.purchase(Fake.account(), threeTickets));
        });

    }

    @Test
    void zero_tickets_for_purchase_are_not_allowed(@Mock ForStorage aStorage, @Mock ForSendingMessage aMessageBroker){
        Set<TicketEntry> noTickets = Set.of();
        App app = App.init(aStorage, aMessageBroker);

        assertThrows(BadPurchaseRequestException.class, () -> {
            app.purchase(Fake.purchase(Fake.account(), noTickets));
        });

    }

    @Test
    void a_null_list_of_tickets_for_purchase_is_not_allowed(@Mock ForStorage aStorage, @Mock ForSendingMessage aMessageBroker){
        Set<TicketEntry> nullTickets = null;
        App app = App.init(aStorage, aMessageBroker);

        assertThrows(Exception.class, () -> {
            app.purchase(Fake.purchase(Fake.account(), nullTickets));
        });

    }

    @Test
    void cannot_purchase_after_soldout(@Mock ForStorage aStorage, @Mock ForSendingMessage aMessageBroker) throws Exception{
        TicketEntry arrivedLate = Fake.ticket(new Event("EVSLDOUT1", new Random().nextInt(10)), Fake.attendee());

        App app = App.init(aStorage, aMessageBroker);

        assertThrows(SoldoutException.class, () -> {
            app.purchase(Fake.purchase(Fake.account(), Set.of(arrivedLate)));
        });
    }

    @Test
    void new_ticket_should_decrease_available_seats_by_one(@Mock ForStorage aStorage, @Mock ForSendingMessage aMessageBroker) throws Exception{
        final String eventId = "EV0001";
        Event event = new Event(eventId, 10);
        TicketEntry newTicket = Fake.ticket(event, Fake.attendee());

        App app = App.init(aStorage, aMessageBroker);

        app.purchase(Fake.purchase(Fake.account(), Set.of(newTicket)));

        assertEquals(9, event.getRemainingSeats());

    }

    @Test
    void event_cannot_be_empty(@Mock ForStorage aStorage, @Mock ForSendingMessage aMessageBroker) throws Exception{

        assertThrows(EventNotFoundException.class, () -> {
            TicketEntry nullEventTicket = new TicketEntry(null, Fake.attendee());
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
        Event event = new Event("0001", new Random().nextInt(10));
        TicketEntry ticketA = Fake.ticket(event, Attendee.createAttendee("AB01", "/", "/", "/", "/"));
        TicketEntry ticketB = Fake.ticket(event, Attendee.createAttendee("AB01", "/", "/", "/", "/"));

        App app = App.init(aStorage, aMessageBroker);

        assertThrows(RuntimeException.class, () -> {
            app.purchase(Fake.purchase(Fake.account(), Set.of(ticketA, ticketB)));
        });



    }

}
