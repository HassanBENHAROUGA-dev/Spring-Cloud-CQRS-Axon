package com.brokers.digitalbanking.command.aggregates;

import com.brokers.digitalbanking.command.BalanceNotSufficientException;
import com.brokers.digitalbanking.coreapi.CreateAccountCommand;
import com.brokers.digitalbanking.coreapi.CreditAccountCommand;
import com.brokers.digitalbanking.coreapi.DebitAccountCommand;
import com.brokers.digitalbanking.coreapi.enums.AccountStatus;
import com.brokers.digitalbanking.coreapi.events.AccountActivatedEvent;
import com.brokers.digitalbanking.coreapi.events.AccountCreatedEvent;
import com.brokers.digitalbanking.coreapi.events.AccountCreditedEvent;
import com.brokers.digitalbanking.coreapi.events.AccountDebitedEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.math.BigDecimal;

@Aggregate
@Slf4j
public class AccountAggregate {
    @AggregateIdentifier//l'attribut qui represente l'identifiant de l'aggregat
    private String accountId;
    private BigDecimal balance;
    private String currency;
    private AccountStatus status;
/*
* So, what exactly is a Command Bus? The role of the Command Bus is to ensure the transport
* of a Command to its Handler. The Command Bus receives a Command, which is nothing more than
* a message describing intent, and passes this onto a Handler which is then responsible for
* performing the expected behavior.*/
/*
* An event bus is a pipeline that receives events. Rules associated with the event bus evaluate events as they arrive.
*/

    public AccountAggregate() {
        //Default Constructor required by Axon!
    }

    @CommandHandler
    //Fonction de décision
    public AccountAggregate(CreateAccountCommand command){
            log.info("CreateAccountCommand received......");
            /* Logique metier/Business logic*/
        AggregateLifecycle.apply(new AccountCreatedEvent(
                //AggregateLifecycle va nous permettre de dispatcher(distribuer) un evennement qui est (AccountCreditedEvent)
                command.getId(),
                command.getInitialBalance(),
                command.getCurrency()
        ));
    }
    /*
    Après l'éxecution de la logique métier et tout se passe bien on met à jour un evennement qui va créer par exemple un compte
    puis cet evennement va etre dispatcher(distribuer)
                    ||
                    ||
                   \_/
        Et  cet event va etre capter par ce handler(fonction de décision)
    */

    @EventSourcingHandler
    //Fonction d'évolution : mettre à jour les données de l'évennement (par exemple on change le solde du compte) et on les transfert dans l'application
    public void on(AccountCreatedEvent event){
        log.info("AccountCreatedEvent Occured....");
        this.accountId=event.getId();
        this.balance=event.getInitialBalance();
        this.currency=event.getCurrency();
        this.status=AccountStatus.CREATED;
        AggregateLifecycle.apply(new AccountActivatedEvent(
                event.getId(),
                AccountStatus.ACTIVATED
        ));
    }
    @EventSourcingHandler
    ///Fonction d'évolution : mettre à jour les données de l'évennement (par exemple on change le solde du compte) et on les transfert dans l'application
    public void on(AccountActivatedEvent event){
        log.info("AccountAcctivatedEvent Occured....");
        this.status=event.getStatus();
    }
    /*
    * Après l'éxecution notre event va etre enrégistrer dans la table domain_event_entry
    *
    * */

    @CommandHandler
    //Fonction de décision
    public void handle(CreditAccountCommand command){
        log.info("CreditAccountCommand received......");
        /* Logique metier/Business logic*/
        AggregateLifecycle.apply(new AccountCreditedEvent(
                //AggregateLifecycle va nous permettre de dispatcher(distribuer) un evennement qui est (AccountCreditedEvent)
                command.getId(),
                command.getAmount(),
                command.getCurrency()
        ));
    }

    @EventSourcingHandler
    //Fonction d'évolution : mettre à jour les données de l'évennement (par exemple on change le solde du compte) et on les transfert dans l'application
    public void on(AccountCreditedEvent event){
        log.info("AccountCreditedEvent Occured....");
        this.balance=this.balance.add(event.getAmount());
    }
    @CommandHandler
    //Fonction de décision
    public void handle(DebitAccountCommand command){
        log.info("CreditAccountCommand received......");
        /* Logique metier/Business logic*/
        if(this.balance.subtract(command.getAmount()).doubleValue()<0){
            throw new BalanceNotSufficientException("Balance Not Sufficient Exception");
        }
        AggregateLifecycle.apply(new AccountDebitedEvent(
                //AggregateLifecycle va nous permettre de dispatcher(distribuer) un evennement qui est (AccountCreditedEvent)
                command.getId(),
                command.getAmount(),
                command.getCurrency()
        ));
    }

    @EventSourcingHandler
    //Fonction d'évolution : mettre à jour les données de l'évennement (par exemple on change le solde du compte) et on les transfert dans l'application
    public void on(AccountDebitedEvent event){
        log.info("AccountDebitedEvent Occured....");
        this.balance=this.balance.subtract(event.getAmount());
    }
    /*
     * Après l'éxecution notre event va etre enrégistrer dans la table domain_event_entry
     *
     * */
}
