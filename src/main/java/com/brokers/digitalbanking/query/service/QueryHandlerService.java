package com.brokers.digitalbanking.query.service;

import com.brokers.digitalbanking.query.DTO.AccountDTO;
import com.brokers.digitalbanking.query.DTO.AccountHistoryDTO;
import com.brokers.digitalbanking.query.DTO.AccountOperationDTO;
import com.brokers.digitalbanking.query.entities.Account;
import com.brokers.digitalbanking.query.entities.AccountOperation;
import com.brokers.digitalbanking.query.mappers.AccountMapper;
import com.brokers.digitalbanking.query.queries.GetAccountByIdQuery;
import com.brokers.digitalbanking.query.queries.GetAccountHistoryQuery;
import com.brokers.digitalbanking.query.queries.GetAccountOperationsQuery;
import com.brokers.digitalbanking.query.repository.AccountOperationRepository;
import com.brokers.digitalbanking.query.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class QueryHandlerService {
    private AccountRepository accountRepository;
    private AccountOperationRepository accountOperationRepository;
    private AccountMapper accountMapper;

    public QueryHandlerService(AccountRepository accountRepository, AccountOperationRepository accountOperationRepository, AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.accountOperationRepository = accountOperationRepository;
        this.accountMapper = accountMapper;
    }

    @QueryHandler
    public AccountDTO handle(GetAccountByIdQuery query){
        Account account = accountRepository.findById(query.getAccountId()).get();
        /*AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(account.getId());
        accountDTO.setBalance(account.getBalance());
        accountDTO.setStatus(account.getStatus());
        accountDTO.setCurrency(account.getCurrency());*/
        AccountDTO accountDTO = accountMapper.fromAccount(account);
        return accountDTO;
    }
    @QueryHandler
    public List<AccountOperationDTO> handle(GetAccountOperationsQuery query){
        List<AccountOperation> operations = accountOperationRepository.findByAccountId(query.getAccountId());
        List<AccountOperationDTO> OperationsDTO = operations
                .stream()
                .map(accOp->accountMapper.fromAccountOperation(accOp))
                .collect(Collectors.toList());
        return OperationsDTO;
    }
    @QueryHandler
    public AccountHistoryDTO handle(GetAccountHistoryQuery query){
        Account account = accountRepository.findById(query.getAccountId()).get();
        AccountDTO accountDTO = accountMapper.fromAccount(account);

        List<AccountOperation> operations = accountOperationRepository.findByAccountId(query.getAccountId());
        List<AccountOperationDTO> OperationsDTO = operations
                .stream()
                .map(accOp->accountMapper.fromAccountOperation(accOp))
                .collect(Collectors.toList());
        return new AccountHistoryDTO(accountDTO,OperationsDTO);
    }


}
