package com.brokers.digitalbanking.coreapi;

import com.brokers.digitalbanking.query.enums.OperationType;
import lombok.Getter;

import java.math.BigDecimal;

public class CreditAccountCommand extends BaseCommand<String>{
    @Getter private BigDecimal amount;
    @Getter private String currency;


    public CreditAccountCommand(String id, BigDecimal amount, String currency) {
        super(id);
        this.amount = amount;
        this.currency = currency;
    }

}
