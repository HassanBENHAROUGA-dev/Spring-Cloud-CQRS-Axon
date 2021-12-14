package com.brokers.digitalbanking.query.service;

import com.brokers.digitalbanking.coreapi.enums.AccountStatus;
import com.brokers.digitalbanking.coreapi.events.AccountActivatedEvent;
import com.brokers.digitalbanking.coreapi.events.AccountCreatedEvent;
import com.brokers.digitalbanking.coreapi.events.AccountCreditedEvent;
import com.brokers.digitalbanking.coreapi.events.AccountDebitedEvent;
import com.brokers.digitalbanking.query.DTO.AccountDTO;
import com.brokers.digitalbanking.query.entities.Account;
import com.brokers.digitalbanking.query.entities.AccountOperation;
import com.brokers.digitalbanking.query.enums.OperationType;
import com.brokers.digitalbanking.query.mappers.AccountMapper;
import com.brokers.digitalbanking.query.queries.GetAccountByIdQuery;
import com.brokers.digitalbanking.query.repository.AccountOperationRepository;
import com.brokers.digitalbanking.query.repository.AccountRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.ResetHandler;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@AllArgsConstructor
@Slf4j
public class EventHandlerService {
    private AccountRepository accountRepository;
    private AccountOperationRepository accountOperationRepository;
    private QueryUpdateEmitter queryUpdateEmitter;
    private AccountMapper accountMapper;

    @ResetHandler
    //pour rejouer un evennement
    public void ResetDatabase(){
        log.info("ResetDatabase.....");
        accountRepository.deleteAll();
        accountOperationRepository.deleteAll();
    }

    @EventHandler
    /*difference entre eventsourcinghandler et eventhandler est que eventsourcinhanlder se fait
    * dans la parite command(aggregat pour mettre à jour/changer l'état de l'application)* alors
    * que eventhandler se fait dans la partie lecture et capte l'événnement*/
    public void on(AccountCreatedEvent event){
        log.info("***************** Query Side *******************");
        log.info("AccountCreatedEvent Occured");
        Account account = new Account();
        account.setId(event.getId());
        account.setBalance(event.getInitialBalance());
        account.setCurrency(event.getCurrency());
        account.setStatus(AccountStatus.CREATED);
        Account savedAccount = accountRepository.save(account);
    }
    @EventHandler
    @Transactional
    public void on(AccountActivatedEvent event){
        log.info("***************** Query Side *******************");
        log.info("AccountActivatedEvent Occured");
        Account account = accountRepository.findById(event.getId()).get();
        account.setStatus(event.getStatus());
        accountRepository.save(account);
    }


    @EventHandler
    @Transactional
    public void on(AccountCreditedEvent event){
        log.info("***************** Query Side *******************");
        log.info("AccountCreditedevent Occured");

        Account account = accountRepository.findById(event.getId()).get();

        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setOperationDate(new Date());
        accountOperation.setAmount(event.getAmount());
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setAccount(account);
        accountOperationRepository.save(accountOperation);
        account.setBalance(account.getBalance().add(event.getAmount()));
        accountRepository.save(account);
        AccountDTO accountDTO = accountMapper.fromAccount(account);
        queryUpdateEmitter.emit(
                message ->
                        ((GetAccountByIdQuery)message.getPayload()).getAccountId().equals(event.getId()),
                accountDTO );

    }

    @EventHandler
    @Transactional
    public void on(AccountDebitedEvent event){
        log.info("***************** Query Side *******************");
        log.info("AccountDebitedEvent Occured");

        Account account = accountRepository.findById(event.getId()).get();

        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setOperationDate(new Date());
        accountOperation.setAmount(event.getAmount());
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setAccount(account);
        accountOperationRepository.save(accountOperation);
        account.setBalance(account.getBalance().subtract(event.getAmount()));
        Account savedAccount = accountRepository.save(account);
        AccountDTO accountDTO = accountMapper.fromAccount(account);
        queryUpdateEmitter.emit(
                /*
                * On va emettre un évennement si le message qu'on va recevoir de la requette du query
                * si AccountId du Payload est egale au meme AccountId du compte auquel
                * se produit l'evennement on va emettre(mettre à jour) l'état du compte
                * */
                message ->
                ((GetAccountByIdQuery)message.getPayload()).getAccountId().equals(event.getId()),
                accountDTO );//getPayLoad()=> common way to obtain message
    }
/*Token_Entry : une table crée par Axon pour tracker chaque event invoquer dans l'application en gardant le token
* qui indique que ce handler a deja jouer son évennement pour éviter de les rejouer une deuxieme fois*/
}
