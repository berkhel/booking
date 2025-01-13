package it.berkhel.booking.unit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import it.berkhel.booking.app.App;
import it.berkhel.booking.app.actionport.ForBooking;
import it.berkhel.booking.app.drivenport.ForSendingMessage;
import it.berkhel.booking.app.drivenport.ForStorage;
import it.berkhel.booking.app.exception.BadPurchaseRequestException;
import it.berkhel.booking.app.exception.EventNotFoundException;
import it.berkhel.booking.app.exception.SoldoutException;
import it.berkhel.booking.entity.Event;
import it.berkhel.booking.entity.Purchase;
import it.berkhel.booking.entity.Ticket;
import it.berkhel.booking.unit.fixture.Fake;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.stream.Stream;


@ExtendWith(MockitoExtension.class)
class UnitTest {

    @Test void booking_targets_the_storage(@Mock ForStorage theStorage, @Mock ForSendingMessage messageBroker) throws Exception {
        ForBooking app = App.init(theStorage, messageBroker);

        app.purchase(List.of(Fake.ticket()));

        verify(theStorage).save(Mockito.any(), Mockito.any());
    }

    @Test void booking_targets_the_message_broker(@Mock ForStorage aStorage, @Mock ForSendingMessage theMessageBroker) throws Exception {
        ForBooking app = App.init(aStorage, theMessageBroker);

        app.purchase(List.of(Fake.ticket()));

        verify(theMessageBroker).sendMessage(Mockito.any(), Mockito.any());
    }

    @Test void booking_normally_return_a_response(@Mock ForStorage aStorage, @Mock ForSendingMessage aMessageBroker) throws Exception {
        ForBooking app = App.init(aStorage, aMessageBroker);

        Purchase thePurchase = app.purchase(List.of(Fake.ticket()));

        assertThat(thePurchase, any(Purchase.class));
    }

    @Test
    void not_more_than_three_tickets_for_purchase(@Mock ForStorage aStorage, @Mock ForSendingMessage aMessageBroker){
        List<Ticket> fourTickets = Stream.generate(Fake::ticket).limit(4).toList();
        ForBooking app = App.init(aStorage, aMessageBroker);

        assertThrows(BadPurchaseRequestException.class, () -> {
            app.purchase(fourTickets);
        });
    }

    @Test
    void three_tickets_for_purchase_are_allowed(@Mock ForStorage aStorage, @Mock ForSendingMessage aMessageBroker){
        List<Ticket> threeTickets = Stream.generate(Fake::ticket).limit(3).toList();
        ForBooking app = App.init(aStorage, aMessageBroker);

        assertDoesNotThrow(() -> {
            app.purchase(threeTickets);
        });

    }

    @Test
    void zero_tickets_for_purchase_are_not_allowed(@Mock ForStorage aStorage, @Mock ForSendingMessage aMessageBroker){
        List<Ticket> noTickets = List.of();
        ForBooking app = App.init(aStorage, aMessageBroker);

        assertThrows(BadPurchaseRequestException.class, () -> {
            app.purchase(noTickets);
        });

    }

    @Test
    void a_null_list_of_tickets_for_purchase_is_not_allowed(@Mock ForStorage aStorage, @Mock ForSendingMessage aMessageBroker){
        List<Ticket> nullTickets = null;
        ForBooking app = App.init(aStorage, aMessageBroker);

        assertThrows(Exception.class, () -> {
            app.purchase(nullTickets);
        });

    }

    @Test
    void cannot_purchase_after_soldout(@Mock ForStorage aStorage, @Mock ForSendingMessage aMessageBroker){
        Event soldoutEvent = new Event("EVSLDOUT1", 100, 0);
        Ticket arrivedLate = new Ticket();
        arrivedLate.setEvent(soldoutEvent);

        ForBooking app = App.init(aStorage, aMessageBroker);

        assertThrows(SoldoutException.class, () -> {
            app.purchase(List.of(arrivedLate));
        });
    }

    @Test
    void new_ticket_should_decrease_available_seats_by_one(@Mock ForStorage aStorage, @Mock ForSendingMessage aMessageBroker) throws Exception{
        Event event = new Event("EV0001", 100, 10);
        Ticket newTicket = new Ticket();
        newTicket.setEvent(event);
        App app = App.init(aStorage, aMessageBroker);

        app.purchase(List.of(newTicket));

        assertEquals(9, event.getRemainingSeats());

    }

    @Test
    void event_cannot_be_empty(@Mock ForStorage aStorage, @Mock ForSendingMessage aMessageBroker) throws Exception{
        Ticket ticket = new Ticket();
        ticket.setEvent(null);
        App app = App.init(aStorage, aMessageBroker);

        assertThrows(EventNotFoundException.class, () -> {
            app.purchase(List.of(ticket));
        });
    }


    @Test
    void can_query_for_events(@Mock ForStorage theStorage, @Mock ForSendingMessage aMessageBroker) throws Exception{

        App app = App.init(theStorage, aMessageBroker);

        verify(theStorage).getEventById(any(String.class));

    }

}
