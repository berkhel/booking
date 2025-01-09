package it.berkhel.booking.drivenadapter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.berkhel.booking.drivenport.ForStorage;
import it.berkhel.booking.entity.Reservation;

@Repository
public class JpaStorage implements ForStorage {

    private JpaRepository<Reservation, String> repo;

    public JpaStorage(JpaRepository<Reservation, String> repo){
        this.repo = repo;
    }


    @Override
    public void save(Reservation aReservation) {
        repo.save(aReservation);
    }

    @Override
    public Reservation retrieveById(String reservationId) {
        return repo.getReferenceById(reservationId);
    }

}
