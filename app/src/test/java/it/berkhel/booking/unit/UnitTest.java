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

import it.berkhel.booking.App;
import it.berkhel.booking.ForBooking;
import it.berkhel.booking.ForStorage;
import it.berkhel.booking.Reservation;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;


@ExtendWith(MockitoExtension.class)
class UnitTest {

    @Test void booking_a_reservation_targets_the_storage(@Mock ForStorage theStorage, @Mock Reservation aReservation) {
        ForBooking app = App.init(theStorage);
        app.book(aReservation);

        verify(theStorage).save(aReservation);
    }

    @Test void booking_always_return_a_response(@Mock ForStorage theStorage, @Mock Reservation aReservation) {
        when(aReservation.getId()).thenReturn(UUID.randomUUID().toString());
        ForBooking app = App.init(theStorage);

        String response = app.book(aReservation);

        assertThat(response, is(notNullValue()));
        assertThat(response, hasLength(greaterThan(0)) );
    }


    @Test void store_and_retrieve_reservations_from_in_memory_storage() {
        ForStorage storage = new InMemoryStorage();
        Reservation firstReservation = new Reservation();
        Reservation secondReservation = new Reservation();
        String firstReservationId = firstReservation.getId();
        String secondReservationId = secondReservation.getId();

        storage.save(firstReservation);
        storage.save(secondReservation);

        assertThat(firstReservation, is(equalTo(
                storage.retrieveById(firstReservationId))));
        assertThat(secondReservation, is(equalTo(
                storage.retrieveById(secondReservationId))));
    }

}
