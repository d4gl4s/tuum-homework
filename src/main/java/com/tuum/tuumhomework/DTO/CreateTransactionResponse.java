package com.tuum.tuumhomework.DTO;

import com.tuum.tuumhomework.enums.Currency;
import com.tuum.tuumhomework.enums.TransactionDirection;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CreateTransactionResponse {
    private Long accountId;
    private Long transactionId;
    private BigDecimal amount;
    private Currency currency;
    private TransactionDirection direction;
    private String description;
    private BigDecimal balanceAfterTransaction;
}

