package com.tuum.tuumhomework.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Account {
    private Long id;
    private Long customerId;
    private String country;
    private List<Balance> balances;
}
