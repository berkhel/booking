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
    private String email;

    @JsonProperty
    @NotBlank
    private String firstName;

    @JsonProperty
    @NotBlank
    private String lastName;

    @JsonProperty
    @NotBlank 
    private String birthDate;


    public Attendee(){}

    public Attendee(@NotBlank String id, @NotBlank String email, @NotBlank String firstName, @NotBlank String lastName,
            @NotBlank String birthDate) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "Attendee [id=" + id + ", firstName=" + firstName + ", email=" + email +", lastName=" + lastName + ", birthDate=" + birthDate
                + "]";
    }

    public String getId() {
        return id;
    }


    
}
