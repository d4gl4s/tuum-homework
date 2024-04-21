package com.tuum.tuumhomework.model;

import com.tuum.tuumhomework.enums.Currency;
import com.tuum.tuumhomework.enums.TransactionDirection;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class Transaction {
    private final Long id;
    private final Long accountId;
    private final BigDecimal amount;
    private final Currency currency;
    private final TransactionDirection direction;
    private final String description;
}
