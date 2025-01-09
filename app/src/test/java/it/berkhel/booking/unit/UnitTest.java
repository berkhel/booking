package it.berkhel.booking.unit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasLength;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import it.berkhel.booking.app.App;
import it.berkhel.booking.app.actionport.ForBooking;
import it.berkhel.booking.app.drivenport.ForStorage;
import it.berkhel.booking.entity.Purchase;
import it.berkhel.booking.unit.fixture.InMemoryStorage;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;


@ExtendWith(MockitoExtension.class)
class UnitTest {

    @Test void booking_a_reservation_targets_the_storage(@Mock ForStorage theStorage) {
        ForBooking app = App.init(theStorage);

        Purchase reservation = app.book();

        verify(theStorage).save(reservation);
    }

    @Test void booking_always_return_a_response(@Mock ForStorage theStorage) {
        ForBooking app = App.init(theStorage);

        Purchase reservation = app.book();

        assertThat(reservation, is(notNullValue()));
        assertThat(reservation.getId(), hasLength(greaterThan(0)) );
    }

    @Test
    void not_more_than_three_tickets_for_reservation(){

    }


}
