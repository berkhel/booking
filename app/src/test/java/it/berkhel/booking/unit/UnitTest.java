package it.berkhel.booking.unit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import it.berkhel.booking.app.App;
import it.berkhel.booking.app.actionport.ForBooking;
import it.berkhel.booking.app.drivenport.ForStorage;
import it.berkhel.booking.app.exception.BadPurchaseRequestException;
import it.berkhel.booking.app.exception.SoldoutException;
import it.berkhel.booking.entity.Event;
import it.berkhel.booking.entity.Purchase;
import it.berkhel.booking.entity.Ticket;
import it.berkhel.booking.unit.fixture.Fake;

import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.stream.Stream;


@ExtendWith(MockitoExtension.class)
class UnitTest {

    @Test void booking_targets_the_storage(@Mock ForStorage theStorage) throws Exception {
        ForBooking app = App.init(theStorage);

        Purchase aPurchase = app.purchase(List.of(Fake.ticket()));

        verify(theStorage).save(aPurchase);
    }

    @Test void booking_normally_return_a_response(@Mock ForStorage aStorage, @Mock Ticket aTicket) throws Exception {
        ForBooking app = App.init(aStorage);

        Purchase thePurchase = app.purchase(List.of(Fake.ticket()));

        assertThat(thePurchase, any(Purchase.class));
    }

    @Test
    void not_more_than_three_tickets_for_purchase(@Mock ForStorage aStorage){
        List<Ticket> fourTickets = Stream.generate(Fake::ticket).limit(4).toList();
        ForBooking app = App.init(aStorage);

        assertThrows(BadPurchaseRequestException.class, () -> {
            app.purchase(fourTickets);
        });
    }

    @Test
    void three_tickets_for_purchase_are_allowed(@Mock ForStorage aStorage){
        List<Ticket> threeTickets = Stream.generate(Fake::ticket).limit(3).toList();
        ForBooking app = App.init(aStorage);

        assertDoesNotThrow(() -> {
            app.purchase(threeTickets);
        });

    }

    @Test
    void zero_tickets_for_purchase_are_not_allowed(@Mock ForStorage aStorage){
        List<Ticket> noTickets = List.of();
        ForBooking app = App.init(aStorage);

        assertThrows(BadPurchaseRequestException.class, () -> {
            app.purchase(noTickets);
        });

    }

    @Test
    void a_null_list_of_tickets_for_purchase_is_not_allowed(@Mock ForStorage aStorage){
        List<Ticket> noTickets = null;
        ForBooking app = App.init(aStorage);

        assertThrows(Exception.class, () -> {
            app.purchase(noTickets);
        });

    }

    @Test
    void cannot_purchase_after_soldout(@Mock ForStorage aStorage){
        Event soldoutEvent = new Event("ID00A1", 100, 0);
        Ticket arrivedLate = new Ticket();
        arrivedLate.setEvent(soldoutEvent);

        ForBooking app = App.init(aStorage);

        assertThrows(SoldoutException.class, () -> {
            app.purchase(List.of(arrivedLate));
        });

    }

}
