package com.brokers.digitalbanking.query.repository;

import com.brokers.digitalbanking.query.entities.AccountOperation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountOperationRepository extends JpaRepository<AccountOperation,Long> {
    List<AccountOperation> findByAccountId(String accountId);
}
