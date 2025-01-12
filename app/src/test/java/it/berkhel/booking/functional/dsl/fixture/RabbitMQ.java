package it.berkhel.booking.functional.dsl.fixture;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.TimeoutException;

import org.testcontainers.containers.RabbitMQContainer;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class RabbitMQ { 

    private final Queue<String> messages = new LinkedList<>();
    private final RabbitMQContainer container;

    public RabbitMQ(RabbitMQContainer rabbitMQContainer) {
        this.container = rabbitMQContainer;
    }

    public Connection getConnection() throws IOException, TimeoutException{
        Integer exposedPort = Optional.ofNullable(container.getExposedPorts().getFirst())
                .orElse(5672);
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(container.getHost());
        factory.setPort(container.getMappedPort(exposedPort));
        factory.setUsername(container.getAdminUsername());
        factory.setPassword(container.getAdminPassword());
        return factory.newConnection();
    }


    // public void sendMessage(String queue, byte[] message) throws IOException, TimeoutException {
    //     try (Connection connection = getConnection()) {
    //         try (Channel channel = connection.createChannel()) {
    //             channel.basicPublish("", queue, null, message);
    //         }
    //     }
    // }

    public String consumedMessage(String queue)
            throws IOException, TimeoutException {
        try (Connection connection = getConnection()) {
            try (Channel channel = connection.createChannel()) {
                channel.basicConsume(queue, new DefaultConsumer(channel) {
                    @Override
                    public void handleDelivery(String consumerTag,
                            Envelope envelope,
                            AMQP.BasicProperties properties,
                            byte[] body)
                            throws IOException {
                        String routingKey = envelope.getRoutingKey();
                        String contentType = properties.getContentType();
                        long deliveryTag = envelope.getDeliveryTag();
                        String message = new String(body, StandardCharsets.UTF_8);
                        messages.add(message);
                        channel.basicAck(deliveryTag, false);
                    }
                });
            }
        }
        return messages.poll();
    }

}
