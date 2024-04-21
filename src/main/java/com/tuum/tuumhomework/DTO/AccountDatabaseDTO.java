package com.tuum.tuumhomework.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountDatabaseDTO {
    private Long id;
    private Long customerId;
    private String country;
}
