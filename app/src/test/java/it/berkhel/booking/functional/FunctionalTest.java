package it.berkhel.booking.functional;

import it.berkhel.booking.app.actionport.ForBooking;
import it.berkhel.booking.config.MainConfig;
import it.berkhel.booking.functional.dsl.fixture.MySqlDatabase;
import static it.berkhel.booking.functional.dsl.fixture.MySqlDatabase.isEqualToRecordIdFrom;

import java.sql.SQLException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import io.restassured.RestAssured;

import static io.restassured.RestAssured.when;


@SpringBootTest(classes = { MainConfig.class }, webEnvironment = WebEnvironment.RANDOM_PORT)
@Testcontainers
public class FunctionalTest {

    @Container
    @ServiceConnection
    @SuppressWarnings("resource")
    private static MySQLContainer<?> mySqlContainer = new MySQLContainer<>(DockerImageName.parse("mysql:5.7.34"))
    .withDatabaseName("booking")
    .withLogConsumer(new Slf4jLogConsumer(LoggerFactory.getLogger(FunctionalTest.class)));


    private static MySqlDatabase mySqlDatabase;


    @BeforeAll static void setupMySqlClient() throws SQLException{
        assert mySqlContainer.isRunning() : "MySQL container is not running";
        Integer exposedPort = Integer.parseInt(Optional.ofNullable(mySqlContainer.getExposedPorts().getFirst().toString())
                .orElse("3306"));
        Integer mappedHostPort = mySqlContainer.getMappedPort(exposedPort);
        mySqlDatabase = new MySqlDatabase(mappedHostPort);
    }

    @BeforeAll 
    static void setupRestClient(@LocalServerPort Integer port){
        RestAssured.baseURI = "http://localhost:"+ port;
    }



    @Test
    void testRestApi(@Autowired ForBooking bookingManager) {

        when().
            post("/booking").
        then().
            statusCode(200).
        and().body("id",
            isEqualToRecordIdFrom(mySqlDatabase, "reservation"));

    }

    
}
