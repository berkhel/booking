package it.berkhel.app;


import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.util.Map;


@ExtendWith(MockitoExtension.class)
class MessageTest {

    @Test void testGetMessage(@Mock ForStorage storage) {
        App app = App.init(storage);
        app.bookReservation();
        verify(storage).storeBooking();
    }
}
