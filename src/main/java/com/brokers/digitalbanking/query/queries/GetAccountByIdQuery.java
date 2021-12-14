package com.brokers.digitalbanking.query.queries;

import lombok.Data;

@Data
public class GetAccountByIdQuery {
    private String accountId;
    public GetAccountByIdQuery(String accountId) {
        this.accountId=accountId;
    }
}
