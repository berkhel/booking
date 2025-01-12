package it.berkhel.email.functional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.text.IsEmptyString.emptyOrNullString;

import java.io.IOException;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import static io.restassured.RestAssured.when;
import static io.restassured.RestAssured.given;

import static org.awaitility.Awaitility.waitAtMost;

import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;

import jakarta.mail.internet.MimeMessage;
import it.berkhel.email.AmqpConfiguration;
import it.berkhel.email.EmailSender;

@SpringBootTest(classes = { AmqpConfiguration.class })
@Testcontainers
public class EmailTest {

    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP.dynamicPort());

    @Container
    @ServiceConnection
    private static RabbitMQContainer rabbitMQContainer = new RabbitMQContainer(DockerImageName.parse("rabbitmq:3.7.25-management-alpine"));



    @Test
    void send_email_after_receiving_a_message(@Autowired EmailSender emailSender, @Autowired AmqpTemplate amqpTemplate) throws Exception {

        Integer dynamicPort = greenMail.getSmtp().getPort();
        System.out.println("EMAIL SENDER HOST:"+emailSender.getHost());
        System.out.println("EMAIL SENDER PORT:"+emailSender.getPort());
        emailSender.setHost("localhost");
        emailSender.setPort(dynamicPort);

        amqpTemplate.convertAndSend("booking","{\"email\":\"test@example.it\",\"message\":\"Hello!\"}");

        waitAtMost(Duration.ofSeconds(15)).untilAsserted(() -> {
            final MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
            assert receivedMessages.length > 0 : "Message not received yet";
            final MimeMessage receivedMessage = receivedMessages[0];
            assertEquals("Hello!", receivedMessage.getContent());
        });
    }

    
}
