package it.berkhel.booking.functional.dsl.fixture;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeoutException;

import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class RabbitMQ {

    private final Queue<String> messages = new LinkedList<>();
    private final ConnectionFactory connectionFactory;

    public RabbitMQ(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public Connection getConnection() throws IOException, TimeoutException {
        return connectionFactory.createConnection();
    }

    public String consumedMessage() {
        System.out.println("MESSAGES->:" + messages);
        return messages.poll();
    }


    public void createQueue(String queue)
            throws IOException, TimeoutException {
        try (Connection connection = getConnection()) {
            try (Channel channel = connection.createChannel(false)) {
                channel.queueDeclare(queue, true, false, false, null);
                channel.basicConsume(queue, new DefaultConsumer(channel) {
                    @Override
                    public void handleDelivery(String consumerTag,
                            Envelope envelope,
                            AMQP.BasicProperties properties,
                            byte[] body)
                            throws IOException {
                        long deliveryTag = envelope.getDeliveryTag();
                        String message = new String(body, StandardCharsets.UTF_8);
                        messages.add(message);
                        channel.basicAck(deliveryTag, false);
                    }
                });
            }
        }
    }

    public Integer getMessagesSize() {
        return messages.size();
    }

    public ConnectionFactory getConnectionFactory() {
        return connectionFactory;
    }

    public void flushMessages() {
        this.messages.clear();
    }

}
