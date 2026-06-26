package com.example.bankapi.controller;

import com.example.bankapi.dto.AccountResponse;
import com.example.bankapi.dto.AmountRequest;
import com.example.bankapi.dto.CreateAccountRequest;
import com.example.bankapi.dto.TransferRequest;
import com.example.bankapi.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService service;

    public AccountController(AccountService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<AccountResponse> create(@Valid @RequestBody CreateAccountRequest request) {
        var created = service.create(request.number(), request.holder());
        var response = AccountResponse.from(created);

        return ResponseEntity
                .created(URI.create("/accounts/" + response.number()))
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<AccountResponse>> findAll() {
        var responses = service.findAll()
                .stream()
                .map(AccountResponse::from)
                .toList();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{number}")
    public ResponseEntity<AccountResponse> getByNumber(@PathVariable String number) {
        var account = service.getByNumber(number);
        return ResponseEntity.ok(AccountResponse.from(account));
    }

    @PostMapping("/{number}/deposit")
    public ResponseEntity<AccountResponse> deposit(
            @PathVariable String number,
            @Valid @RequestBody AmountRequest request
    ) {
        var updated = service.deposit(number, request.amount());
        return ResponseEntity.ok(AccountResponse.from(updated));
    }

    @PostMapping("/{number}/withdraw")
    public ResponseEntity<AccountResponse> withdraw(
            @PathVariable String number,
            @Valid @RequestBody AmountRequest request
    ) {
        var updated = service.withdraw(number, request.amount());
        return ResponseEntity.ok(AccountResponse.from(updated));
    }

    @PostMapping("/transfer")
    public ResponseEntity<Void> transfer(@Valid @RequestBody TransferRequest request) {
        service.transfer(request.fromAccount(), request.toAccount(), request.amount());
        return ResponseEntity.ok().build();
    }
}
