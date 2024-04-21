package com.tuum.tuumhomework.controller;

import com.tuum.tuumhomework.DTO.CreateAccountRequest;
import com.tuum.tuumhomework.DTO.CreateAccountResponse;
import com.tuum.tuumhomework.DTO.GetAccountResponse;
import com.tuum.tuumhomework.exceptions.InvalidInputException;
import com.tuum.tuumhomework.exceptions.ResourceNotFoundException;
import com.tuum.tuumhomework.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/create")
    public ResponseEntity<CreateAccountResponse> createAccount(@RequestBody CreateAccountRequest request) throws InvalidInputException {
        CreateAccountResponse response = accountService.createAccount(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<GetAccountResponse> getAccount(@PathVariable Long accountId) throws ResourceNotFoundException {
        GetAccountResponse response = accountService.getAccount(accountId);
        return ResponseEntity.ok(response);
    }
}
