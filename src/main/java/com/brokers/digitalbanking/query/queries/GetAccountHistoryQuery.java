package com.brokers.digitalbanking.query.queries;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class GetAccountHistoryQuery {
    private String accountId;
    public GetAccountHistoryQuery(String accountId) {
        this.accountId=accountId;
    }
}
