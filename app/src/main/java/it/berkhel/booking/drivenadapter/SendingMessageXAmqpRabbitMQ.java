package it.berkhel.booking.drivenadapter;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import it.berkhel.booking.app.drivenport.ForSendingMessage;
import it.berkhel.booking.entity.Attendee;

@Component
public class SendingMessageXAmqpRabbitMQ implements ForSendingMessage{

    
    private final RabbitTemplate rabbitTemplate;
    private final Queue queue;
    

    public SendingMessageXAmqpRabbitMQ(
        RabbitTemplate rabbitTemplate, Queue queue) {
        this.rabbitTemplate = rabbitTemplate;
        this.queue = queue;
    }

    @Override
    public void sendMessage(Attendee recipient, String message) {
        String jsonMsg = "{\"email\":\"" + recipient.getEmail() + "\", \"message\":\"" + message + "\"}";
        rabbitTemplate.convertAndSend(queue.getName(), jsonMsg);
    }
}
