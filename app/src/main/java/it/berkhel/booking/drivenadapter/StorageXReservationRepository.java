package it.berkhel.booking.drivenadapter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.berkhel.booking.app.drivenport.ForStorage;
import it.berkhel.booking.entity.Purchase;

@Repository
public class StorageXReservationRepository implements ForStorage {

    private JpaRepository<Purchase, String> repo;

    public StorageXReservationRepository(JpaRepository<Purchase, String> repo){
        this.repo = repo;
    }


    @Override
    public void save(Purchase aReservation) {
        repo.save(aReservation);
    }

    @Override
    public Purchase retrieveById(String reservationId) {
        return repo.getReferenceById(reservationId);
    }

}
