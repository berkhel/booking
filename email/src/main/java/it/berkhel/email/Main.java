package it.berkhel.email;

import it.berkhel.email.EmailSender;

public class Main {

    public static void main(String[] args) {
        new EmailSender("smtp.yopmail.com", 25)
        .sendEmail("mim@example.it", "alt.bl-1o9xryyt@yopmail.com", "Hello Pippo!");
        System.out.println("Starting email server");
    }
    
}
