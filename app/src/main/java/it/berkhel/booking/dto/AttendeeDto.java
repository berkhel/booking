package it.berkhel.booking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;

public class AttendeeDto {
    
    @JsonProperty
    @NotBlank
    public String id;

    @JsonProperty
    @NotBlank
    String email;

    @JsonProperty
    @NotBlank
    String firstName;

    @JsonProperty
    @NotBlank
    String lastName;

    @JsonProperty
    @NotBlank
    String birthDate;

    public void setId(String id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }


}
