package it.berkhel.booking;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestApiController {


    @GetMapping("/booking")
    public String hello() {
       return "Hello World!";
    }
    
}
