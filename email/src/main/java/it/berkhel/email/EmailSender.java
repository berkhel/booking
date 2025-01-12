package it.berkhel.email;

import org.simplejavamail.email.EmailBuilder;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.mailer.MailerBuilder;
import org.simplejavamail.api.mailer.config.TransportStrategy;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import it.berkhel.email.Message;

@Component
@RabbitListener(queuesToDeclare = @Queue("${custom.rabbitmq.queue.name}"))
public class EmailSender {

    @Value("${custom.simplejavamail.host}")
    private String host;

    @Value("${custom.simplejavamail.port}")
    private Integer port;


    public String getHost() {
        return host;
    }

    public Integer getPort() {
        return port;
    }


    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    @RabbitHandler
    public void receive(String msg) throws Exception {
        System.out.println("RECEIVED MESSAGE:"+msg);
        Message message = Message.parse(msg);
       this.sendEmail("from@example.it",message.getEmail(), message.getMessage());
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
