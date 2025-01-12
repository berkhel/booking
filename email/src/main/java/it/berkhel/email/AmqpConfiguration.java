package it.berkhel.email;


import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.core.RabbitAdmin;

@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(basePackages = {"it.berkhel.email"})
public class AmqpConfiguration {

    // @Bean
    // public RabbitAdmin admin(ConnectionFactory cf) {
    //     return new RabbitAdmin(cf);
    // }

    // @Bean
    // Queue queue(AmqpAdmin amqpAdmin, @Value("${custom.rabbitmq.queue.name}") String queueName){
    //     Queue queue = new Queue(queueName);
    //     if(amqpAdmin.declareQueue(queue) == null) throw new RuntimeException("Cannot declare queues");
    //     return queue; 
    // }
    
}
