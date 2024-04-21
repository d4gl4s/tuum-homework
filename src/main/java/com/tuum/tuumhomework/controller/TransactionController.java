package com.tuum.tuumhomework.controller;

import com.tuum.tuumhomework.DTO.CreateTransactionRequest;
import com.tuum.tuumhomework.DTO.CreateTransactionResponse;
import com.tuum.tuumhomework.exceptions.InsufficientFundsException;
import com.tuum.tuumhomework.exceptions.InvalidInputException;
import com.tuum.tuumhomework.exceptions.ResourceNotFoundException;
import com.tuum.tuumhomework.model.Transaction;
import com.tuum.tuumhomework.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private TransactionService transactionService;

    @PostMapping("/create")
    public ResponseEntity<CreateTransactionResponse> createTransaction(@RequestBody CreateTransactionRequest request)
            throws InvalidInputException,
                    InsufficientFundsException,
                    ResourceNotFoundException {
        CreateTransactionResponse response = transactionService.createTransaction(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<List<Transaction>> getAccount(@PathVariable Long accountId) throws ResourceNotFoundException {
        List<Transaction> response = transactionService.getTransactions(accountId);
        return ResponseEntity.ok(response);
    }
}
