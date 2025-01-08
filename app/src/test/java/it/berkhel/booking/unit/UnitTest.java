package it.berkhel.booking.unit;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import it.berkhel.booking.App;
import it.berkhel.booking.ForBooking;
import it.berkhel.booking.ForStorage;
import it.berkhel.booking.InMemoryStorage;
import it.berkhel.booking.Reservation;

import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class UnitTest {

    @Test void booking_a_reservation_targets_the_storage(@Mock ForStorage theStorage, @Mock Reservation aReservation) {
        ForBooking app = App.init(theStorage);
        app.book(aReservation);
        verify(theStorage).save(aReservation);
    }

    @Test void booking_always_return_a_response(@Mock ForStorage theStorage, @Mock Reservation aReservation) {
        ForBooking app = App.init(theStorage);
        String response = app.book(aReservation);
        assertNotNull(response);
        assertTrue(response.length() > 0);
    }


    @Test void store_and_retrieve_reservations_from_in_memory_storage() {
        ForStorage storage = new InMemoryStorage();
        Reservation firstReservation = new Reservation();
        Reservation secondReservation = new Reservation();
        String firstReservationId = firstReservation.getId();
        String secondReservationId = secondReservation.getId();
        storage.save(firstReservation);
        storage.save(secondReservation);
        assertEquals(firstReservation, storage.retrieveById(firstReservationId));
        assertEquals(secondReservation, storage.retrieveById(secondReservationId));
    }

}
