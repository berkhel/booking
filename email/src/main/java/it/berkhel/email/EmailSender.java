package it.berkhel.email;

import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.mailer.MailerBuilder;

public class EmailSender {


    public void sendEmail(String from, String to, String body){

        Email email = EmailBuilder.startingBlank()
                .from("test sender", from)
                .to("test receiver", to)
                .withPlainText(body)
                .buildEmail();
        
        Mailer mailer = MailerBuilder
                .withSMTPServer("localhost", 3025)
                .withDebugLogging(true)
                .buildMailer();

        mailer.sendMail(email);

    }
    
}
