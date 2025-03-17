package it.berkhel.booking.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import it.berkhel.booking.app.entity.Account;


public interface AccountRepository extends JpaRepository<Account, String> {

}
