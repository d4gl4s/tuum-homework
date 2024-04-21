package com.tuum.tuumhomework.model;

import com.tuum.tuumhomework.enums.Currency;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class Balance {

    private BigDecimal availableAmount;
    private final Currency currency;
}
