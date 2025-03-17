package it.berkhel.booking.app.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Account {

    @Id
    private String id;

    private Account(){}

    public Account(String id){
        this.id = id;
    }
    

}
