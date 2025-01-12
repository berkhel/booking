package it.berkhel.booking.drivenadapter;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
        try {
            System.out.println("HOST* :"+rabbitTemplate.getConnectionFactory().getHost());
            System.out.println("PORT* :"+rabbitTemplate.getConnectionFactory().getPort());
            System.out.println("USERNAME* :"+ rabbitTemplate.getConnectionFactory().getUsername());
            System.out.println("VHOST* :"+ rabbitTemplate.getConnectionFactory().getVirtualHost());
            System.out.println("QUEUE* :"+queue);
        String jsonMsg = "{\"email\":\""+recipient.getEmail()+"\", \"message\":\""+message+"\"}";
        rabbitTemplate.convertAndSend(queue.getName(), jsonMsg);
        }catch(RuntimeException ex){

            System.out.println("EXCEPTION SENDING MESSAGE:"+ex.getMessage());
        }
    }
}
