package com.tuum.tuumhomework.service;


import com.tuum.tuumhomework.DTO.CreateAccountRequest;
import com.tuum.tuumhomework.DTO.CreateAccountResponse;
import com.tuum.tuumhomework.DTO.GetAccountResponse;
import com.tuum.tuumhomework.enums.Currency;
import com.tuum.tuumhomework.exceptions.InvalidInputException;
import com.tuum.tuumhomework.exceptions.ResourceNotFoundException;
import com.tuum.tuumhomework.mapper.AccountMapper;
import com.tuum.tuumhomework.model.Account;
import com.tuum.tuumhomework.model.Balance;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountMapper accountMapper;

    @Transactional
    public CreateAccountResponse createAccount(CreateAccountRequest request) throws InvalidInputException {

        // Creates initial balances for the account in given currencies
        if(request.getCurrencies() == null || request.getCurrencies().isEmpty())
            throw new InvalidInputException("Please provide currencies with account");

        List<Balance> balances = new ArrayList<>();
        for (Currency currency : request.getCurrencies()) {
            // No need to check if currency is valid, since that is done in the string to currency converter
            Balance balance = Balance.builder().availableAmount(BigDecimal.ZERO).currency(currency).build();
            balances.add(balance);
        }

        // Create account
        Account account =  Account.builder()
                .country(request.getCountry())
                .balances(balances)
                .customerId(request.getCustomerId())
                .build();

        // Insert account, generates ID value as well
        accountMapper.insertAccount(account);

        // Return response
        return CreateAccountResponse.builder()
                .accountId(account.getId())
                .customerId(account.getCustomerId())
                .balances(account.getBalances())
                .build();
    }

    public GetAccountResponse getAccount(Long accountId) throws ResourceNotFoundException {
        // Get account
        Account account = accountMapper.getAccountById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with given id: " + accountId));

        // Return response
        return GetAccountResponse.builder()
                .accountId(accountId)
                .customerId(account.getCustomerId())
                .balances(account.getBalances())
                .build();
    }
}

