package com.brokers.digitalbanking.query.repository;

import com.brokers.digitalbanking.query.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account,String> {

}
