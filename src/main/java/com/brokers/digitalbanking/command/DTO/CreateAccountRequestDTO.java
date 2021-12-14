package com.brokers.digitalbanking.command.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data @NoArgsConstructor @AllArgsConstructor
public class CreateAccountRequestDTO {
    private BigDecimal initialBalance;
    private String currency;

}
