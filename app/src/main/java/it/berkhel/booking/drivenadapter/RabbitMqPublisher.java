package it.berkhel.booking.drivenadapter;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import it.berkhel.booking.app.drivenport.ForSendingMessage;
import it.berkhel.booking.app.entity.Attendee;

@Component
public class RabbitMqPublisher implements ForSendingMessage{

    
    private final RabbitTemplate rabbitTemplate;
    private final String queueName;
    

    public RabbitMqPublisher(RabbitTemplate rabbitTemplate,
            @Value("${custom.rabbitmq.queue.name}") String queueName) {
        this.rabbitTemplate = rabbitTemplate;
        this.queueName = queueName;
    }

    @Override
    public void sendMessage(Attendee recipient, String message) {
        String jsonMsg = "{\"email\":\"" + recipient.getEmail() + "\", \"message\":\"" + message + "\"}";
        rabbitTemplate.convertAndSend(queueName, jsonMsg);
    }
}
