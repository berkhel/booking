package it.berkhel.booking.functional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;

public class FunctionalTest {

    @BeforeAll 
    static void setupRestClient(){
        RestAssured.baseURI = "http://localhost:8080";
    }

    @Test
    void testRestApi() {

        when().
            get("/booking").
        then().
            statusCode(200);

    }
    
}
