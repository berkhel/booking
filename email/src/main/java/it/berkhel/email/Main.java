package it.berkhel.email;

import org.springframework.boot.SpringApplication;
import it.berkhel.email.AmqpConfiguration;

public class Main {

    public static void main(String[] args) {
		SpringApplication.run(AmqpConfiguration.class, args);
        // var emailSender = new EmailSender();
        // emailSender.sendEmail("mim@example.it", "alt.bl-1o9xryyt@yopmail.com", "Hello Pippo!");
        // System.out.println("Starting email server");
    }
    
}


