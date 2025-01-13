package it.berkhel.booking.functional;

import it.berkhel.booking.config.MainConfig;
import it.berkhel.booking.functional.dsl.fixture.Fake;
import it.berkhel.booking.functional.dsl.fixture.MySqlDatabase;
import it.berkhel.booking.functional.dsl.fixture.RabbitMQ;

import static it.berkhel.booking.functional.dsl.fixture.MySqlDatabase.existsAsValueIn;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.text.IsEmptyString.emptyOrNullString;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import static io.restassured.RestAssured.when;
import static io.restassured.RestAssured.given;

import static org.awaitility.Awaitility.waitAtMost;


@SpringBootTest(classes = { MainConfig.class }, webEnvironment = WebEnvironment.RANDOM_PORT)
@Testcontainers
public class FunctionalTest {

    @Container
    @ServiceConnection
    @SuppressWarnings("resource")
    private static MySQLContainer<?> mySqlContainer = new MySQLContainer<>(DockerImageName.parse("mysql:5.7.34"))
    .withDatabaseName("booking")
    .withLogConsumer(new Slf4jLogConsumer(LoggerFactory.getLogger(FunctionalTest.class)));

    @Container
    @ServiceConnection
    private static RabbitMQContainer rabbitMQContainer = new RabbitMQContainer(DockerImageName.parse("rabbitmq:3.7.25-management-alpine"));


    private static MySqlDatabase mySqlDatabase;
    private static RabbitMQ rabbitmq;


    @BeforeAll static void setupMySqlClient() throws SQLException{
        assert mySqlContainer.isRunning() : "MySQL container is not running";
        Integer exposedPort = Integer.parseInt(Optional.ofNullable(mySqlContainer.getExposedPorts().getFirst().toString())
                .orElse("3306"));
        Integer mappedHostPort = mySqlContainer.getMappedPort(exposedPort);
        mySqlDatabase = new MySqlDatabase(mappedHostPort);
    }

    @BeforeAll static void setupRabbitMqClient(@Autowired ConnectionFactory connectionFactory) {
        assert rabbitMQContainer.isRunning() : "RabbitMQ container is not running";
        rabbitmq = new RabbitMQ(connectionFactory);
    }

    @BeforeAll 
    static void setupRestClient(@LocalServerPort Integer port){
        RestAssured.baseURI = "http://localhost:"+ port;
        RestAssured.requestSpecification = given().contentType(ContentType.JSON);

    }

    @AfterEach
    void deleteAllRecordsFromTables(){
        mySqlDatabase.deleteAllRecords();
    }


    @Test
    void with_a_void_array_should_return_bad_request() {

        given().
            body("[]").
        when().
            post("/booking").
        then().
            statusCode(400);

    }

    @Test
    void with_an_array_with_empty_object_should_return_bad_request() {

        given().
            body("[{}]").
        when().
            post("/booking").
        then().
            statusCode(400);

    }

    @Test
    void malformed_body_should_return_bad_request() {

        given().
            body("[{").
        when().
            post("/booking").
        then().
            statusCode(400);

    }

    @Test
    void without_eventId_should_return_bad_request() {
        String missingEventId = 
                "[" +
                    "{" +
                        "\"eventId\":\"\"," +
                        "\"attendee\": {" +
                            "\"id\": \"ABCD0001\"," +
                            "\"firstName\":\"Mario\"," +
                            "\"lastName\": \"Rossi\"," +
                            "\"birthDate\":\"1990-01-01\"" +
                        "}" +
                    "}" +
                "]";

        given().
            body(missingEventId).
        when().
            post("/booking").
        then().
            statusCode(400);

    }

    @Test
    void without_attendeeId_should_return_bad_request() {
        String missingEventId = 
                "[" +
                    "{" +
                        "\"eventId\":\"0001\"," +
                        "\"attendee\": {" +
                            "\"id\": \"\"," +
                            "\"firstName\":\"Mario\"," +
                            "\"lastName\": \"Rossi\"," +
                            "\"birthDate\":\"1990-01-01\"" +
                        "}" +
                    "}" +
                "]";

        given().
            body(missingEventId).
        when().
            post("/booking").
        then().
            statusCode(400);

    }

    @Test
    void without_a_body_should_return_bad_request() {

        when().
            post("/booking").
        then().
            statusCode(400);

    }

    @Test
    void a_successful_ticket_purchase_should_return_a_resume() throws SQLException {

        mySqlDatabase.createEvent("0001", 10, 10);


        String singlePurchase = 
                "[" +
                    "{" +
                        "\"eventId\":\"0001\"," +
                        "\"attendee\": {" +
                            "\"id\": \"ABCD0001\"," +
                            "\"email\":\"mario.rossi@example.it\","+
                            "\"firstName\":\"Mario\"," +
                            "\"lastName\": \"Rossi\"," +
                            "\"birthDate\":\"1990-01-01\"" +
                        "}" +
                    "}" +
                "]";

        given().
            body(singlePurchase).
        when().
            post("/booking").
        then().
            statusCode(200).
        and().body("tickets[0].attendee.id", equalTo("ABCD0001")).
        and().body("tickets[0].attendee.email", equalTo("mario.rossi@example.it")).
        and().body("tickets[0].attendee.firstName", equalTo("Mario")).
        and().body("tickets[0].attendee.lastName", equalTo("Rossi")).
        and().body("tickets[0].attendee.birthDate", equalTo("1990-01-01")).
        and().body("tickets[0].eventId", equalTo("0001")).
        and().body("tickets[0].id", not(emptyOrNullString())).
        and().body("id", not(emptyOrNullString()));

    }

    @Test
    void a_successful_ticket_purchase_should_insert_records_into_purchase_and_ticket_tables() throws SQLException {

        mySqlDatabase.createEvent("0001", 10, 10);

        given().
            body(Fake.singlePurchaseForEvent("0001")).
        when().
            post("/booking").
        then().
            statusCode(200).
        and().body("id",
            existsAsValueIn(mySqlDatabase, "purchase", "id")).
        and().body("id",
            existsAsValueIn(mySqlDatabase, "ticket", "purchase_id"));


    }


    @Test
    void a_successful_ticket_purchase_should_decrement_event_available_seats() throws SQLException {

        mySqlDatabase.createEvent("0001", 10, 1);

        given().
            body(Fake.singlePurchaseForEvent("0001")).
        when().
            post("/booking").
        then().
            statusCode(200);
        
        assertThat("0", equalTo(mySqlDatabase.select("remaining_seats")
                .from("event")
                .where("id", "=", "0001")
                .query()));

    }

    @Test
    void a_ticket_request_for_a_soldout_event_should_return_bad_request() throws SQLException {

        mySqlDatabase.createEvent("0001", 10, 0);

        given().
            body(Fake.singlePurchaseForEvent("0001")).
        when().
            post("/booking").
        then().
            statusCode(400);

    }
    
    @Test
    void a_successful_ticket_purchase_should_send_an_email_to_the_attendee(
        @Value("${custom.rabbitmq.queue.name}") String queueName)
            throws SQLException, IOException, TimeoutException {

        rabbitmq.createQueue(queueName);
        mySqlDatabase.createEvent("0001", 10, 10);

        given()
            .body(Fake.singlePurchaseForEvent("0001")).
        when()
            .post("/booking").
        then()  
            .statusCode(200);

        waitAtMost(Duration.ofSeconds(10)).untilAsserted(() -> {
            assertThat(rabbitmq.consumedMessage(),
                    startsWith("{\"email\":\"mario.rossi@example.it\", \"message\":\"Here's your ticket:"));
        });

    }

    @Test
    void a_ticket_with_a_non_valid_event_should_tell_it_in_the_response() throws SQLException {


        given().
            body(Fake.singlePurchaseForEvent("0001")).
        when().
            post("/booking").
        then().
            statusCode(400).
        and().
            body("detail",
                    startsWith( "Event not found" ));

    }

    @Test
    void two_ticket_for_two_different_events_should_be_stored_in_database() throws SQLException {

        mySqlDatabase.createEvent("FIRSTEVENT", 10, 10);
        mySqlDatabase.createEvent("SECONDEVENT", 10, 10);


        String twoTicketPurchase = Fake.Purchase.json()
                .withTicket(Fake.Ticket.json().random().withEvent("FIRSTEVENT"))
                .withTicket(Fake.Ticket.json().random().withEvent("SECONDEVENT"))
                .build();
        
        System.out.println("FIRST PURCHASE : " + twoTicketPurchase );

        given().
            body(twoTicketPurchase).
        when().
            post("/booking").
        then().
            statusCode(200);
        
        assertThat("2", equalTo(mySqlDatabase.select("count(*)")
                .from("ticket")
                .query()));

        assertThat("1", equalTo(mySqlDatabase.select("count(*)")
                .from("purchase")
                .query()));
    }


    @Test
    void a_ticket_for_the_same_attendee_and_event_cannot_be_purchased_twice() throws SQLException {

        mySqlDatabase.createEvent("SAMEEVENT", 10, 10);
        mySqlDatabase.createEvent("OTHEREVENT1", 10, 10);
        mySqlDatabase.createEvent("OTHEREVENT2", 10, 10);

        String sameEventId = "SAMEEVENT";
        String sameAttendeeId = "SAMEATTENDEE";

        Fake.Ticket sameTicket = Fake.Ticket.json()
                    .withEvent(sameEventId)
                    .withAttendee(Fake.Attendee.json().withId(sameAttendeeId));


        String firstPurchase = Fake.Purchase.json()
                .withTicket(sameTicket)
                .withTicket(Fake.Ticket.json().random().withEvent("OTHEREVENT1"))
                .build();
        
        given().
            body(firstPurchase).
        when().
            post("/booking").
        then().
            statusCode(200);
        


        String secondPurchase = Fake.Purchase.json()
                .withTicket(Fake.Ticket.json().random().withEvent("OTHEREVENT2"))
                .withTicket(sameTicket)
                .build();

        given().
            body(secondPurchase).
        when().
            post("/booking").
        then().
            statusCode(400).
        and().
            body("detail",
                    startsWith( "Ticket was already purchased in a previous session for" ));

    }

    @Test
    void event_created_should_be_stored_in_database() throws SQLException {

        String newEvent = "{\"id\":\"0001\",\"maxSeats\":10,\"remainingSeats\":10}";

        given().
            body(newEvent).
        when().
            post("/event").
        then().
            statusCode(200);

        assertThat("1", equalTo(mySqlDatabase.select("count(*)")
                .from("event")
                .where("id", "=", "0001")
                .query()));
    }

    @Test
    void cannot_created_an_event_twice() throws SQLException {

        //mySqlDatabase.createEvent("0001", 10, 10);

        String newEvent = "{\"id\":\"0001\",\"maxSeats\":10,\"remainingSeats\":10}";

        given().
            body(newEvent).
        when().
            post("/event").
        then().
            statusCode(200);

        given().
            body(newEvent).
        when().
            post("/event").
        then().
            statusCode(400).
        and().
            body("detail", startsWith("Event already exists"));
        

        assertThat("1", equalTo(mySqlDatabase.select("count(*)")
                .from("event")
                .where("id", "=", "0001")
                .query()));
    }

    @Test
    void cannot_created_an_event_with_negative_remaining_seats() throws SQLException {


        String newEvent = "{\"id\":\"0045\",\"maxSeats\":10,\"remainingSeats\":-1}";

        given().
            body(newEvent).
        when().
            post("/event").
        then().
            statusCode(400);

    }

}