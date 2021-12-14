package com.brokers.digitalbanking.coreapi.events;

import lombok.Getter;

import java.math.BigDecimal;

public class AccountCreatedEvent extends BaseEvent<String>{
    @Getter private BigDecimal initialBalance;
    @Getter private String currency;

    public AccountCreatedEvent(String id, BigDecimal initialBalance, String currency) {
        super(id);
        this.initialBalance = initialBalance;
        this.currency = currency;
    }
}
