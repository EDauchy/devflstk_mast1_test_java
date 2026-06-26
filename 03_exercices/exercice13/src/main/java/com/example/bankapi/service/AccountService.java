package com.example.bankapi.service;

import com.example.bankapi.exception.AccountAlreadyExistsException;
import com.example.bankapi.exception.AccountNotFoundException;
import com.example.bankapi.exception.InsufficientFundsException;
import com.example.bankapi.exception.InvalidAmountException;
import com.example.bankapi.model.Account;
import com.example.bankapi.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountService {

    private final AccountRepository repository;

    public AccountService(AccountRepository repository) {
        this.repository = repository;
    }

    public Account create(String number, String holder) {
        if (number == null || number.isBlank()) {
            throw new IllegalArgumentException("Le numéro de compte est obligatoire");
        }

        if (holder == null || holder.isBlank()) {
            throw new IllegalArgumentException("Le titulaire est obligatoire");
        }

        String trimmedNumber = number.trim();
        if (repository.existsByNumber(trimmedNumber)) {
            throw new AccountAlreadyExistsException(trimmedNumber);
        }

        Account account = new Account(trimmedNumber, holder.trim(), BigDecimal.ZERO);
        return repository.save(account);
    }

    public Account getByNumber(String number) {
        return repository.findByNumber(number)
                .orElseThrow(() -> new AccountNotFoundException(number));
    }

    public List<Account> findAll() {
        return repository.findAll();
    }

    public Account deposit(String number, BigDecimal amount) {
        validatePositiveAmount(amount);
        Account account = getByNumber(number);
        Account updated = new Account(
                account.number(),
                account.holder(),
                account.balance().add(amount)
        );
        return repository.save(updated);
    }

    public Account withdraw(String number, BigDecimal amount) {
        validatePositiveAmount(amount);
        Account account = getByNumber(number);

        if (account.balance().compareTo(amount) < 0) {
            throw new InsufficientFundsException(number, account.balance(), amount);
        }

        Account updated = new Account(
                account.number(),
                account.holder(),
                account.balance().subtract(amount)
        );
        return repository.save(updated);
    }

    public void transfer(String fromAccount, String toAccount, BigDecimal amount) {
        validatePositiveAmount(amount);

        Account source = repository.findByNumber(fromAccount)
                .orElseThrow(() -> new AccountNotFoundException(fromAccount));

        if (repository.findByNumber(toAccount).isEmpty()) {
            throw new AccountNotFoundException(toAccount);
        }

        if (source.balance().compareTo(amount) < 0) {
            throw new InsufficientFundsException(fromAccount, source.balance(), amount);
        }

        withdraw(fromAccount, amount);
        deposit(toAccount, amount);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    private void validatePositiveAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException();
        }
    }
}
