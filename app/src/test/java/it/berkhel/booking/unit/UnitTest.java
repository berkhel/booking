package it.berkhel.booking.unit;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import it.berkhel.booking.App;
import it.berkhel.booking.ForBooking;
import it.berkhel.booking.ForStorage;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.util.Map;


@ExtendWith(MockitoExtension.class)
class UnitTest {

    @Test void test_dumb_booking(@Mock ForStorage storage) {
        ForBooking app = App.init(storage);
        app.book();
        verify(storage).storeBooking();
    }

    @Test void test_booking_return_a_confirmation(@Mock ForStorage storage) {
        ForBooking app = App.init(storage);
        assertNotNull(app.book());
    }
}
