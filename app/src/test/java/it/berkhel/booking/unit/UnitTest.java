package it.berkhel.booking.unit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasLength;
import static org.hamcrest.Matchers.notNullValue;
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
import it.berkhel.booking.entity.Purchase;
import it.berkhel.booking.entity.Ticket;
import it.berkhel.booking.unit.fixture.InMemoryStorage;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;
import java.util.stream.Stream;


@ExtendWith(MockitoExtension.class)
class UnitTest {

    @Test void booking_a_reservation_targets_the_storage(@Mock ForStorage theStorage, @Mock Ticket aTicket) throws Exception {
        ForBooking app = App.init(theStorage);

        Purchase aPurchase = app.purchase(List.of(aTicket));

        verify(theStorage).save(aPurchase);
    }

    @Test void booking_always_return_a_response(@Mock ForStorage aStorage, @Mock Ticket aTicket) throws Exception {
        ForBooking app = App.init(aStorage);

        Purchase thePurchase = app.purchase(List.of(aTicket));

        assertThat(thePurchase, is(notNullValue()));
        assertThat(thePurchase.getId(), hasLength(greaterThan(0)) );
    }

    @Test
    void not_more_than_three_tickets_for_reservation(@Mock ForStorage aStorage){
        List<Ticket> fourTickets = Stream.generate(Ticket::new).limit(4).toList();
        ForBooking app = App.init(aStorage);

        assertThrows(Exception.class, () -> {
            app.purchase(fourTickets);
        });
        

    }

    @Test
    void three_tickets_for_reservation_are_allowed(@Mock ForStorage aStorage){
        List<Ticket> threeTickets = Stream.generate(Ticket::new).limit(3).toList();
        ForBooking app = App.init(aStorage);

        assertDoesNotThrow(() -> {
            app.purchase(threeTickets);
        });

    }

    @Test
    void zero_tickets_for_reservation_are_not_allowed(@Mock ForStorage aStorage){
        List<Ticket> noTickets = List.of();
        ForBooking app = App.init(aStorage);

        assertThrows(Exception.class, () -> {
            app.purchase(noTickets);
        });

    }


}
