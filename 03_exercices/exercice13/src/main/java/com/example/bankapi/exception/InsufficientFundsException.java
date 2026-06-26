package com.example.bankapi.exception;

import java.math.BigDecimal;

public class InsufficientFundsException extends RuntimeException {

    public InsufficientFundsException(String number, BigDecimal balance, BigDecimal amount) {
        super("Fonds insuffisants sur le compte " + number
                + " (solde: " + balance + ", montant demandé: " + amount + ")");
    }
}
