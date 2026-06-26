package com.example.bankapi.exception;

public class InvalidAmountException extends RuntimeException {

    public InvalidAmountException() {
        super("Le montant doit être strictement positif");
    }
}
