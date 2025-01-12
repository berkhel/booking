package it.berkhel.email;


import org.springframework.context.annotation.Bean;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.amqp.core.Queue;


@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(basePackages = {"it.berkhel.email"})
public class AmqpConfiguration {

    @Bean
    Queue queue(){
        return new Queue("booking");
    }
    
    // @Bean
    // SimpleRabbitListenerContainerFactory rabbitListenerFactory(ConnectionFactory connectionFactory){
    //     var factory = new SimpleRabbitListenerContainerFactory(); 
    //     factory.setConnectionFactory(connectionFactory);
    //     return factory;
    // }
}
