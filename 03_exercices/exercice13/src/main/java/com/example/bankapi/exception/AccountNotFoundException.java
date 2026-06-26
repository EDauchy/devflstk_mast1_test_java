package com.example.bankapi.exception;

public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(String number) {
        super("Aucun compte trouvé avec le numéro " + number);
    }
}
