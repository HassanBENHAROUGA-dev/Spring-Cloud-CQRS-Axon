package com.brokers.digitalbanking.query.mappers;

import com.brokers.digitalbanking.query.DTO.AccountDTO;
import com.brokers.digitalbanking.query.DTO.AccountOperationDTO;
import com.brokers.digitalbanking.query.entities.Account;
import com.brokers.digitalbanking.query.entities.AccountOperation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {
     AccountDTO fromAccount(Account account);
     Account fromAccountDTO(AccountDTO accountDTO);
     AccountOperationDTO fromAccountOperation(AccountOperation accountOperation);
}
