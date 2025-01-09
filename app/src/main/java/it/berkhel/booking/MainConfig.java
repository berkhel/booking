package it.berkhel.booking;

import org.springframework.context.annotation.Bean;

import java.sql.SQLException;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.service.connection.ConnectionDetails;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;


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
