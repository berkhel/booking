package it.berkhel.booking.drivenadapter;

import org.springframework.stereotype.Repository;

import it.berkhel.booking.app.drivenport.ForStorage;
import it.berkhel.booking.entity.Purchase;
import it.berkhel.booking.repository.PurchaseRepository;

@Repository
public class StorageXJpaRepository implements ForStorage {

    private PurchaseRepository repo;

    public StorageXJpaRepository(PurchaseRepository repo){
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
