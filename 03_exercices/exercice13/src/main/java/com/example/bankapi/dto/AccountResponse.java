package com.example.bankapi.dto;

import com.example.bankapi.model.Account;

import java.math.BigDecimal;

public record AccountResponse(String number, String holder, BigDecimal balance) {

    public static AccountResponse from(Account account) {
        return new AccountResponse(account.number(), account.holder(), account.balance());
    }
}
