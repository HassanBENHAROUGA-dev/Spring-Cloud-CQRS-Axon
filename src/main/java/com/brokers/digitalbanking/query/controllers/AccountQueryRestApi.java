package com.brokers.digitalbanking.query.controllers;

import com.brokers.digitalbanking.query.DTO.AccountDTO;
import com.brokers.digitalbanking.query.DTO.AccountHistoryDTO;
import com.brokers.digitalbanking.query.DTO.AccountOperationDTO;
import com.brokers.digitalbanking.query.queries.GetAccountByIdQuery;
import com.brokers.digitalbanking.query.queries.GetAccountHistoryQuery;
import com.brokers.digitalbanking.query.queries.GetAccountOperationsQuery;
import lombok.AllArgsConstructor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(path = "/query/accounts")
@AllArgsConstructor
public class AccountQueryRestApi {
    private QueryGateway queryGateway;//pour dispatcher une requette
    @GetMapping(path = "/{accountId}")
    public CompletableFuture<AccountDTO> getAccount(@PathVariable String accountId){
        CompletableFuture<AccountDTO> query = queryGateway.query(new GetAccountByIdQuery(accountId),
                AccountDTO.class//type de response de la requete =>AccountDTO.class
        );
        return query;
    }

    @GetMapping(path = "/{accountId}/operations")
    public CompletableFuture<List<AccountOperationDTO>> getAccountOperations(@PathVariable String accountId){
        CompletableFuture<List<AccountOperationDTO>> query = queryGateway.query(new GetAccountOperationsQuery(accountId),
                ResponseTypes.multipleInstancesOf(AccountOperationDTO.class));//ResponseTypes.multipleInstancesOf(AccountOperationDTO.class)=> nous permet de returner plusieurs instances de type AccountOperationDTO donc une Liste de AccountOperationDTO
        return query;
    }

    @GetMapping(path = "/{accountId}/history")
    public CompletableFuture<AccountHistoryDTO> getAccountHistory(@PathVariable String accountId){
        CompletableFuture<AccountHistoryDTO> query = queryGateway.query(new GetAccountHistoryQuery(accountId),
                ResponseTypes.instanceOf(AccountHistoryDTO.class));//Retourner une seule instance de type AccountHistoryDTO
        return query;
    }
     //quand il y'a une requette http il fait subscribe et après quand il y'a des evennements il les pousse du serveur vers le client
    //Technique HTTP SSE : SERVER SENT EVENT
    @GetMapping(path = "{accountId}/watch",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<AccountDTO> subscribeToAccount(@PathVariable String accountId){
        SubscriptionQueryResult<AccountDTO, AccountDTO> result = queryGateway.subscriptionQuery(new GetAccountByIdQuery(accountId),
                ResponseTypes.instanceOf(AccountDTO.class),//l'instance qu'on va récupérer serait de type AccountDTO
                ResponseTypes.instanceOf(AccountDTO.class)//Quand il y'aura des mise à jour on va returrner également une instance de type AccountDTO
        );
        return result.initialResult().concatWith(result.updates());//dés qu'il y'a des mise à jours(update) coté backend on le pousse vers le Flux qui pouuse les données vers la partie UX
    }
}
