package it.berkhel.app;


import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

import java.util.Map;


@ExtendWith(MockitoExtension.class)
class MessageTest {

    @Test void testGetMessage(@Mock Message message) {
        when(message.getMessage()).thenReturn("Ciao Mondo!");
        assertEquals("Ciao Mondo!", message.getMessage());
    }
}
