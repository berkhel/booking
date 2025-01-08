package it.berkhel.booking.unit;


import static org.junit.jupiter.api.Assertions.assertNotNull;
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

    @Test void book_a_reservation(@Mock ForStorage storage) {
        Reservation aReservation = new Reservation();
        ForBooking app = App.init(storage);
        app.book(aReservation);
        verify(storage).storeBooking(aReservation);
    }

    @Test void booking_and_return_a_confirmation(@Mock ForStorage storage) {
        ForBooking app = App.init(storage);
        assertNotNull(app.book(null));
    }


    @Test void store_and_retrieve_from_in_memory_storage() {
        ForStorage storage = new InMemoryStorage();
        Reservation aReservation = new Reservation();
        storage.storeBooking(aReservation);
        fail();
    }

}
