package com.brokers.digitalbanking.query.DTO;

import com.brokers.digitalbanking.query.DTO.AccountDTO;
import com.brokers.digitalbanking.query.DTO.AccountOperationDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor
public class AccountHistoryDTO {
    private AccountDTO accountDTO;
    private List<AccountOperationDTO> operations;
}
