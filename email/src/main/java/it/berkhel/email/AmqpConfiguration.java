package it.berkhel.email;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

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
