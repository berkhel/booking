package it.berkhel.booking.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Attendee {

    @Id
    @JsonProperty
    @NotBlank
    private String id;

    @JsonProperty
    @NotBlank
    private String firstName;

    @JsonProperty
    @NotBlank
    private String lastName;

    @JsonProperty
    @NotBlank 
    private String birthDate;

    @Override
    public String toString() {
        return "Attendee [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", birthDate=" + birthDate
                + "]";
    }

    
}
