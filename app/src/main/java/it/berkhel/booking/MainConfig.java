package it.berkhel.booking;


import org.springframework.context.annotation.Bean;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(basePackages = {"it.berkhel.booking"})
public class MainConfig {

    @Bean
    ForStorage storage(){
        return new ForStorage() {
            @Override
            public void storeBooking(Reservation reservation) {
               //do nothing 
            }
        };
    }


    @Bean
    ForBooking bookingManager(ForStorage storage){
        return App.init(storage);
    }
    
}
