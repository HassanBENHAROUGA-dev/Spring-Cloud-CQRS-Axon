package com.brokers.digitalbanking.command.DTO;

import com.brokers.digitalbanking.query.enums.OperationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor @AllArgsConstructor
public class CreditAccountRequestDTO {
    private String accountId;
    private BigDecimal amount;
    private String currency;
}
