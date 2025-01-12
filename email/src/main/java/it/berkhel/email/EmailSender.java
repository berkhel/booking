package it.berkhel.email;

import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.mailer.MailerBuilder;
import org.simplejavamail.api.mailer.config.TransportStrategy;

public class EmailSender {

    private String host;
    private Integer port;

    public EmailSender(String host, Integer port){
        this.host = host;
        this.port = port;
    }

    public void sendEmail(String from, String to, String body){

        Email email = EmailBuilder.startingBlank()
                .from("test sender", from)
                .to("test receiver", to)
                .withPlainText(body)
                .buildEmail();
        
        Mailer mailer = MailerBuilder
                .withSMTPServer(host, port)
                .withDebugLogging(true)
                .buildMailer();

        mailer.sendMail(email);

    }
    
}
