package com.tuum.tuumhomework.service;

import com.tuum.tuumhomework.DTO.AccountDatabaseDTO;
import com.tuum.tuumhomework.DTO.CreateTransactionRequest;
import com.tuum.tuumhomework.DTO.CreateTransactionResponse;
import com.tuum.tuumhomework.DTO.GetAccountResponse;
import com.tuum.tuumhomework.enums.TransactionDirection;
import com.tuum.tuumhomework.exceptions.InsufficientFundsException;
import com.tuum.tuumhomework.exceptions.InvalidInputException;
import com.tuum.tuumhomework.exceptions.ResourceNotFoundException;
import com.tuum.tuumhomework.mapper.AccountMapper;
import com.tuum.tuumhomework.mapper.TransactionMapper;
import com.tuum.tuumhomework.model.Account;
import com.tuum.tuumhomework.model.Balance;
import com.tuum.tuumhomework.model.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionMapper transactionMapper;
    private final AccountMapper accountMapper;
    private final AccountService accountService;

    @Transactional
    public CreateTransactionResponse createTransaction(CreateTransactionRequest request) throws InvalidInputException, ResourceNotFoundException, InsufficientFundsException{

        // Check if amount is valid
        if(request.getAmount().compareTo(BigDecimal.ZERO) <= 0)
            throw new InvalidInputException("Invalid transaction amount. Amount can not be negative or zero");

        GetAccountResponse account = accountService.getAccount(request.getAccountId());

        // Check if account has sufficient funds
        Balance accountBalance = null;

        outer:
        if (TransactionDirection.OUT.equals(request.getDirection())) {
            for (Balance balance : account.getBalances()) {
                if (balance.getCurrency().equals(request.getCurrency())){
                    accountBalance = balance;
                    if (balance.getAvailableAmount().compareTo(request.getAmount()) >= 0)
                        break outer;
                    else throw new InsufficientFundsException("Insufficient funds. Not enough balance for transaction.");
                }
            }
            throw new InvalidInputException("Invalid currency. Account does not have balance with such currency.");
        }

        // Check if description is missing
        if (request.getDescription().isBlank())
            throw new InvalidInputException("Transaction description missing");

        BigDecimal newBalance = calculateNewBalance(request.getDirection(),
                request.getAmount(), accountBalance);

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
        accountBalance.setAvailableAmount(newBalance);
        accountMapper.updateAccountBalance(account.getAccountId(), accountBalance);

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

    private BigDecimal calculateNewBalance(TransactionDirection direction, BigDecimal amount, Balance accountBalance){
        if(direction.equals(TransactionDirection.OUT))
            return accountBalance.getAvailableAmount().subtract(amount);
        return accountBalance.getAvailableAmount().add(amount);
    }

    public List<Transaction> getTransactions(Long accountId) throws ResourceNotFoundException{
        // Check if account is valid
       // accountMapper.getAccountById(accountId)
         //       .orElseThrow(() -> new ResourceNotFoundException("Account not found with given id: " + accountId));

        return transactionMapper.getTransactionsByAccountId(accountId);
    }
}
