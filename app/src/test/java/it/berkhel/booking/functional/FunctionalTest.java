package it.berkhel.booking.functional;

import it.berkhel.booking.ForBooking;
import it.berkhel.booking.ForStorage;
import it.berkhel.booking.MainConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;

import io.restassured.RestAssured;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;


@SpringBootTest(classes = { MainConfig.class }, webEnvironment = WebEnvironment.RANDOM_PORT)
public class FunctionalTest {


    @BeforeAll 
    static void setupRestClient(@LocalServerPort Integer port){
        RestAssured.baseURI = "http://localhost:"+ port;
    }


    @Test
    void testRestApi(@Autowired ForBooking bookingManager) {

        when().
            get("/booking").
        then().
            statusCode(200).
        and().
            body(equalTo(bookingManager.book()));

    }

    
}
