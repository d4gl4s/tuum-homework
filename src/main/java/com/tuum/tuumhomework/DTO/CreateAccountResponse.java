package com.tuum.tuumhomework.DTO;

import com.tuum.tuumhomework.model.Balance;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CreateAccountResponse {
    private Long accountId;
    private Long customerId;
    private List<Balance> balances;
}
