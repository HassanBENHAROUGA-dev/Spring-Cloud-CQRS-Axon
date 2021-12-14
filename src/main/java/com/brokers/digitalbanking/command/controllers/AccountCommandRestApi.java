package com.brokers.digitalbanking.command.controllers;

import com.brokers.digitalbanking.command.DTO.CreateAccountRequestDTO;
import com.brokers.digitalbanking.command.DTO.CreditAccountRequestDTO;
import com.brokers.digitalbanking.command.DTO.DebitAccountRequestDTO;
import com.brokers.digitalbanking.coreapi.CreateAccountCommand;
import com.brokers.digitalbanking.coreapi.CreditAccountCommand;
import com.brokers.digitalbanking.coreapi.DebitAccountCommand;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@RestController
@RequestMapping(path = "/commands/accounts")
@Slf4j//fonctionnalité Lombok pour logger un message
@AllArgsConstructor
public class AccountCommandRestApi {
    private EventStore eventStore;
    private CommandGateway commandGateway;//CommandGateway nous permet de dispatcher/récupérer une command(command veux dire ici un attribut de Axon Framework qui permet d'envoyer des messages)
   /* public AccountCommandRestApi(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }*/

        @PostMapping(path = "/create")
        public CompletableFuture<String> NewAccount(@RequestBody CreateAccountRequestDTO request){
            log.info("CreateAccountRequestDTO => "+request.getInitialBalance().toString());
            /*
            * A CompletableFuture is an extension to Java's Future API which was introduced in Java 8.
            * A Future is used for asynchronous Programming. It provides two methods, isDone() and get().
            * The methods retrieve the result of the computation(calcul) when it completes.
            * */
            CompletableFuture<String> response = commandGateway.send(new CreateAccountCommand(
                    UUID.randomUUID().toString(),
                    request.getInitialBalance(),
                    request.getCurrency()
            ));
            return response;
        }

    @PutMapping(path = "/credit")
    public CompletableFuture<String> Credit(@RequestBody CreditAccountRequestDTO request){
        log.info("CreditAccountRequestDTO => ");
        /*
         * A CompletableFuture is an extension to Java's Future API which was introduced in Java 8.
         * A Future is used for asynchronous Programming. It provides two methods, isDone() and get().
         * The methods retrieve the result of the computation(calcul) when it completes.
         * */
        CompletableFuture<String> response = commandGateway.send(new CreditAccountCommand(
                request.getAccountId(),
                request.getAmount(),
                request.getCurrency()));
        return response;
    }
    @PutMapping(path = "/debit")
    public CompletableFuture<String> Debit(@RequestBody DebitAccountRequestDTO request){
        log.info("DebitAccountRequestDTO => ");
        /*
         * A CompletableFuture is an extension to Java's Future API which was introduced in Java 8.
         * A Future is used for asynchronous Programming. It provides two methods, isDone() and get().
         * The methods retrieve the result of the computation(calcul) when it completes.
         * */
        CompletableFuture<String> response = commandGateway.send(new DebitAccountCommand(
                request.getAccountId(),
                request.getAmount(),
                request.getCurrency()
        ));
        return response;
    }
        @ExceptionHandler(Exception.class)
        public ResponseEntity<String> exceptionHandler(Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.MULTI_STATUS.INTERNAL_SERVER_ERROR);
        }
        @GetMapping(path="/events/{accountId}")
        public Stream AccountEvents(@PathVariable String accountId){
            return  eventStore.readEvents(accountId).asStream();
        }
}
