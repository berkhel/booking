package it.berkhel.email;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;

import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;

import jakarta.mail.internet.MimeMessage;

import it.berkhel.email.EmailSender;

public class EmailTest {

    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP);

    @Test
    void simpleTest() throws Exception {
        new EmailSender().sendEmail("from@example.it","to@example.it","Hello!");
        final MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
        final MimeMessage receivedMessage = receivedMessages[0];
        assertEquals("Hello!", receivedMessage.getContent());
    }

    
}
