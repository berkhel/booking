package it.berkhel.email;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import it.berkhel.email.Email;

public class EmailTest {

    @Test
    void simpleTest(){
        assertEquals("Hello!", new Email().sayHello());
    }
    
}
