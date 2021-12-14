package com.brokers.digitalbanking.query.queries;

import lombok.Data;

@Data
public class GetAccountOperationsQuery {
    private String accountId;
    public GetAccountOperationsQuery(String accountId) {
        this.accountId=accountId;
    }
}
