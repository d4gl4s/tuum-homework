package com.tuum.tuumhomework.DTO;

import com.tuum.tuumhomework.enums.Currency;
import lombok.Data;

import java.util.List;

@Data
public class CreateAccountRequest {
    private Long customerId;
    private String country;
    private List<Currency> currencies;
}
