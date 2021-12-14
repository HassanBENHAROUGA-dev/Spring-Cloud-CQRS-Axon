package com.brokers.digitalbanking.query.DTO;

import com.brokers.digitalbanking.query.entities.Account;
import com.brokers.digitalbanking.query.enums.OperationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor @AllArgsConstructor
public class AccountOperationDTO {
    private Long id;
    private Date operationDate;
    private BigDecimal amount;
    private OperationType type;
}
