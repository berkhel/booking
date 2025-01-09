package it.berkhel.booking;

import org.springframework.context.annotation.Bean;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import it.berkhel.booking.actionport.ForBooking;
import it.berkhel.booking.drivenport.ForStorage;


@SpringBootConfiguration
@EnableAutoConfiguration
@EnableJpaRepositories
@ComponentScan(basePackages = {"it.berkhel.booking"})
public class MainConfig {


    @Bean
    ForBooking bookingManager(ForStorage storage){
        return App.init(storage);
    }
    
}
