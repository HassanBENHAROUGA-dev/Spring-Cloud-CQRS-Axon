package com.brokers.digitalbanking.query.DTO;

import com.brokers.digitalbanking.coreapi.enums.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {
    private String id;
    private BigDecimal balance;
    private String currency;
    private AccountStatus status;
}
