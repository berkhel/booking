package it.berkhel.email.dsl.fixture;

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

    private final ConnectionFactory connectionFactory;

    public RabbitMQ(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public Connection getConnection() throws IOException, TimeoutException {
        return connectionFactory.createConnection();
    }

    public void publishOnQueue(String queue, String message)
            throws IOException, TimeoutException {
        try (Connection connection = getConnection()) {
            try (Channel channel = connection.createChannel(false)) {
                channel.queueDeclare(queue, true, false, false, null);
                byte[] messageBodyBytes = message.getBytes("UTF-8");
                channel.basicPublish("", queue, null, messageBodyBytes);
            }
        }
    }
}