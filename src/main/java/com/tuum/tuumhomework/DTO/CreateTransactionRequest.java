package com.tuum.tuumhomework.DTO;

import com.tuum.tuumhomework.enums.Currency;
import com.tuum.tuumhomework.enums.TransactionDirection;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateTransactionRequest {
    private Long accountId;
    private BigDecimal amount;
    private Currency currency;
    private TransactionDirection direction;
    private String description;
}
