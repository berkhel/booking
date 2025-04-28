package it.berkhel.booking.app.entity;

import java.util.HashMap;
import java.util.Map;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Attendee {

    @Id
    private String id;

    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "birth_date")
    private String birthDate;


    private Attendee(){} // for JPA

    public static Map<String,Attendee> registry = new HashMap<>();

    public static Attendee createAttendee(String id, String email, String firstName, String lastName,
            String birthDate) {
        Attendee attendee = null;
        if(registry.containsKey(id)){
           attendee = registry.get(id);
           attendee.email = email;
           attendee.firstName = firstName;
           attendee.lastName = lastName;
           attendee.birthDate = birthDate;
        }else{
          attendee = new Attendee(id, email, firstName, lastName, birthDate);
        }
        registry.put(id, attendee);
        return attendee;
    }

    private Attendee(String id, String email, String firstName, String lastName,
            String birthDate) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
    }



    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    @Override
    public String toString() {
        return "Attendee [id=" + id + ", firstName=" + firstName + ", email=" + email +", lastName=" + lastName + ", birthDate=" + birthDate
                + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Attendee other = (Attendee) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    public void merge(Attendee other){
        if(this == other)return;
        this.email = other.email;
        this.firstName = other.firstName;
        this.lastName = other.lastName;
        this.birthDate = other.birthDate;
    }
    
}
