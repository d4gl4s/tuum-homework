package com.tuum.tuumhomework.service;

import com.tuum.tuumhomework.DTO.AccountDatabaseDTO;
import com.tuum.tuumhomework.DTO.CreateAccountRequest;
import com.tuum.tuumhomework.DTO.CreateAccountResponse;
import com.tuum.tuumhomework.DTO.GetAccountResponse;
import com.tuum.tuumhomework.enums.Currency;
import com.tuum.tuumhomework.enums.ExchangeName;
import com.tuum.tuumhomework.enums.RoutingKey;
import com.tuum.tuumhomework.exceptions.InvalidInputException;
import com.tuum.tuumhomework.exceptions.ResourceNotFoundException;
import com.tuum.tuumhomework.mapper.AccountMapper;
import com.tuum.tuumhomework.model.Account;
import com.tuum.tuumhomework.model.Balance;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountMapper accountMapper;
    private final RabbitTemplate rabbitTemplate;

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

       insertAccountWithBalances(account);

        // Publish message to RabbitMQ
        rabbitTemplate.convertAndSend(
                ExchangeName.ACCOUNT.getName(),
                RoutingKey.ACCOUNT_CREATED.getKey(),
                "Account created with id: " + account.getId());

        // Return response
        return CreateAccountResponse.builder()
                .accountId(account.getId())
                .customerId(account.getCustomerId())
                .balances(account.getBalances())
                .build();
    }

    private void insertAccountWithBalances( Account account) {
        // Insert account
        accountMapper.insertAccount(account);

        // Insert balances
        for (Balance balance : account.getBalances()) {
            accountMapper.insertBalance(account.getId(), balance);
        }
    }

    public GetAccountResponse getAccount(Long accountId) throws ResourceNotFoundException {
        AccountDatabaseDTO account = accountMapper.getAccountById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with given id: " + accountId));
        List<Balance> balances = accountMapper.getBalancesByAccountId(accountId);

        // Return response
        return GetAccountResponse.builder()
                .accountId(accountId)
                .customerId(account.getCustomerId())
                .balances(balances)
                .build();
    }

    public boolean isAccountInvalid(Long accountId) {
        AccountDatabaseDTO account = accountMapper.getAccountById(accountId).orElse(null);
        return account != null;
    }

    public void updateAccountBalance(Long accountId, Balance newBalance) throws ResourceNotFoundException {
        // Make sure account exists
        if(isAccountInvalid(accountId)) throw new ResourceNotFoundException("Account not found with given id: " + accountId);

        // Update balance
        accountMapper.updateAccountBalance(accountId, newBalance);

        // Publish message to RabbitMQ
        rabbitTemplate.convertAndSend(
                ExchangeName.ACCOUNT.getName(),
                RoutingKey.ACCOUNT_UPDATED.getKey(),
                "account id: " + accountId + ", new balance: " + newBalance.getAvailableAmount());
    }
}

