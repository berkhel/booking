package it.berkhel.email;

import it.berkhel.email.EmailSender;

public class Main {

    public static void main(String[] args) {
        var emailSender = new EmailSender();
        emailSender.sendEmail("mim@example.it", "alt.bl-1o9xryyt@yopmail.com", "Hello Pippo!");
        System.out.println("Starting email server");
    }
    
}
