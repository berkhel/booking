package it.berkhel.booking.config;

import org.springframework.context.annotation.Bean;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import it.berkhel.booking.app.App;
import it.berkhel.booking.app.actionport.ForBooking;
import it.berkhel.booking.app.drivenport.ForSendingMessage;
import it.berkhel.booking.app.drivenport.ForStorage;


@SpringBootConfiguration
@EnableAutoConfiguration
@EnableJpaRepositories(basePackages = {"it.berkhel.booking.repository"})
@EntityScan("it.berkhel.booking.app.entity")
@ComponentScan(basePackages = {"it.berkhel.booking.controller","it.berkhel.booking.service","it.berkhel.booking.drivenadapter", "it.berkhel.booking.dto"})
public class MainConfig {


    @Bean
    ForBooking bookingManager(ForStorage storage, ForSendingMessage messageBroker){
        return App.init(storage, messageBroker);
    }
    
}
