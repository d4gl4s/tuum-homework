package com.tuum.tuumhomework.service;

import com.tuum.tuumhomework.DTO.CreateTransactionRequest;
import com.tuum.tuumhomework.DTO.CreateTransactionResponse;
import com.tuum.tuumhomework.DTO.GetAccountResponse;
import com.tuum.tuumhomework.enums.ExchangeName;
import com.tuum.tuumhomework.enums.RoutingKey;
import com.tuum.tuumhomework.enums.TransactionDirection;
import com.tuum.tuumhomework.exceptions.InsufficientFundsException;
import com.tuum.tuumhomework.exceptions.InvalidInputException;
import com.tuum.tuumhomework.exceptions.ResourceNotFoundException;
import com.tuum.tuumhomework.mapper.TransactionMapper;
import com.tuum.tuumhomework.model.Balance;
import com.tuum.tuumhomework.model.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionMapper transactionMapper;
    private final AccountService accountService;
    private final RabbitTemplate rabbitTemplate;

    @Transactional
    public CreateTransactionResponse createTransaction(CreateTransactionRequest request) throws InvalidInputException, ResourceNotFoundException, InsufficientFundsException{

        // Check if amount is valid
        if(request.getAmount().compareTo(BigDecimal.ZERO) <= 0)
            throw new InvalidInputException("Invalid transaction amount. Amount can not be negative or zero");

        GetAccountResponse account = accountService.getAccount(request.getAccountId());

        // find the relevant balance
        Balance relevantBalance = null;
        for (Balance accountBalance : account.getBalances()) {
            if(accountBalance.getCurrency().equals(request.getCurrency())){
                relevantBalance = accountBalance;
                break;
            }
        }
        if (relevantBalance == null) throw new InvalidInputException("Invalid currency. Account does not have balance with such currency.");

        // Check if account has sufficient funds
        if (TransactionDirection.OUT.equals(request.getDirection())) {
            if (relevantBalance.getAvailableAmount().compareTo(request.getAmount()) < 0)
                throw new InsufficientFundsException("Insufficient funds. Not enough balance for transaction.");
        }

        // Check if description is missing
        if (request.getDescription().isBlank())
            throw new InvalidInputException("Transaction description missing");

        BigDecimal newBalance = calculateNewBalance(request, relevantBalance);

        Transaction transaction = Transaction.builder()
                .accountId(request.getAccountId())
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .direction(request.getDirection())
                .description(request.getDescription())
                .build();

        // Insert transaction, generates ID as well
        transactionMapper.insertTransaction(transaction);

        // Change account balance
        relevantBalance.setAvailableAmount(newBalance);
        accountService.updateAccountBalance(account.getAccountId(), relevantBalance);

        // Publish message to RabbitMQ
        rabbitTemplate.convertAndSend(
                ExchangeName.TRANSACTION.getName(),
                RoutingKey.TRANSACTION_CREATED.getKey(),
                "Transaction created with id: " + transaction.getId());

        // Here we could use a mapper to simplify the code here
        return CreateTransactionResponse.builder()
                .transactionId(transaction.getId())
                .accountId(transaction.getAccountId())
                .amount(transaction.getAmount())
                .balanceAfterTransaction(newBalance)
                .description(transaction.getDescription())
                .direction(transaction.getDirection())
                .currency(transaction.getCurrency())
                .build();
    }

    private BigDecimal calculateNewBalance(CreateTransactionRequest request, Balance relevantBalance) throws InvalidInputException{
        if(request.getDirection().equals(TransactionDirection.OUT))
            return relevantBalance.getAvailableAmount().subtract(request.getAmount());
        return relevantBalance.getAvailableAmount().add(request.getAmount());
    }

    public List<Transaction> getTransactions(Long accountId) throws ResourceNotFoundException{
        // Check if account is valid
        if(accountService.isAccountInvalid(accountId)) throw new ResourceNotFoundException("Account not found with given id: " + accountId);
        return transactionMapper.getTransactionsByAccountId(accountId);
    }
}
