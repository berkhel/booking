package it.berkhel.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.berkhel.booking.entity.Purchase;

public interface PurchaseRepository extends JpaRepository<Purchase, String> {

}
