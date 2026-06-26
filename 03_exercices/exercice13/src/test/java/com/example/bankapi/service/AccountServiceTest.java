package com.example.bankapi.service;

import com.example.bankapi.exception.AccountAlreadyExistsException;
import com.example.bankapi.exception.AccountNotFoundException;
import com.example.bankapi.exception.InsufficientFundsException;
import com.example.bankapi.exception.InvalidAmountException;
import com.example.bankapi.model.Account;
import com.example.bankapi.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository repository;

    @InjectMocks
    private AccountService service;

    @Test
    void shouldCreateAccount_whenNumberAndHolderAreValid() {
        // Arrange
        when(repository.existsByNumber("FR001")).thenReturn(false);
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Account result = service.create("FR001", "Alice Martin");

        // Assert
        assertEquals("FR001", result.number());
        assertEquals("Alice Martin", result.holder());
        assertEquals(0, BigDecimal.ZERO.compareTo(result.balance()));
        verify(repository).save(any());
    }

    @Test
    void shouldThrowConflict_whenNumberAlreadyExists() {
        // Arrange
        when(repository.existsByNumber("FR001")).thenReturn(true);

        // Act + Assert
        assertThrows(AccountAlreadyExistsException.class,
                () -> service.create("FR001", "Alice Martin"));
        verify(repository, never()).save(any());
    }

    @Test
    void shouldReturnAccount_whenNumberExists() {
        // Arrange
        Account account = new Account("FR001", "Alice", new BigDecimal("100.00"));
        when(repository.findByNumber("FR001")).thenReturn(Optional.of(account));

        // Act
        Account result = service.getByNumber("FR001");

        // Assert
        assertEquals("Alice", result.holder());
        verify(repository).findByNumber("FR001");
    }

    @Test
    void shouldThrowNotFound_whenNumberDoesNotExist() {
        // Arrange
        when(repository.findByNumber("FR999")).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(AccountNotFoundException.class, () -> service.getByNumber("FR999"));
    }

    @Test
    void shouldReturnAllAccounts() {
        // Arrange
        List<Account> accounts = List.of(
                new Account("FR001", "Alice", BigDecimal.ZERO),
                new Account("FR002", "Bob", BigDecimal.ZERO)
        );
        when(repository.findAll()).thenReturn(accounts);

        // Act
        List<Account> result = service.findAll();

        // Assert
        assertEquals(2, result.size());
        verify(repository).findAll();
    }

    @Test
    void shouldDeposit_whenAmountIsValid() {
        // Arrange
        Account existing = new Account("FR001", "Alice", new BigDecimal("50.00"));
        Account updated = new Account("FR001", "Alice", new BigDecimal("150.00"));
        when(repository.findByNumber("FR001")).thenReturn(Optional.of(existing));
        when(repository.save(updated)).thenReturn(updated);

        // Act
        Account result = service.deposit("FR001", new BigDecimal("100.00"));

        // Assert
        assertEquals(0, new BigDecimal("150.00").compareTo(result.balance()));
        verify(repository).save(updated);
    }

    @Test
    void shouldThrowInvalidAmount_whenDepositAmountIsNull() {
        // Act + Assert
        assertThrows(InvalidAmountException.class, () -> service.deposit("FR001", null));
        verify(repository, never()).save(any());
    }

    @Test
    void shouldThrowInvalidAmount_whenDepositAmountIsZero() {
        // Act + Assert
        assertThrows(InvalidAmountException.class, () -> service.deposit("FR001", BigDecimal.ZERO));
        verify(repository, never()).save(any());
    }

    @Test
    void shouldThrowInvalidAmount_whenDepositAmountIsNegative() {
        // Act + Assert
        assertThrows(InvalidAmountException.class,
                () -> service.deposit("FR001", new BigDecimal("-10.00")));
        verify(repository, never()).save(any());
    }

    @Test
    void shouldWithdraw_whenAmountIsValidAndBalanceIsSufficient() {
        // Arrange
        Account existing = new Account("FR001", "Alice", new BigDecimal("200.00"));
        Account updated = new Account("FR001", "Alice", new BigDecimal("150.00"));
        when(repository.findByNumber("FR001")).thenReturn(Optional.of(existing));
        when(repository.save(updated)).thenReturn(updated);

        // Act
        Account result = service.withdraw("FR001", new BigDecimal("50.00"));

        // Assert
        assertEquals(0, new BigDecimal("150.00").compareTo(result.balance()));
        verify(repository).save(updated);
    }

    @Test
    void shouldThrowInvalidAmount_whenWithdrawAmountIsNull() {
        // Act + Assert
        assertThrows(InvalidAmountException.class, () -> service.withdraw("FR001", null));
        verify(repository, never()).save(any());
    }

    @Test
    void shouldThrowInvalidAmount_whenWithdrawAmountIsZero() {
        // Act + Assert
        assertThrows(InvalidAmountException.class, () -> service.withdraw("FR001", BigDecimal.ZERO));
        verify(repository, never()).save(any());
    }

    @Test
    void shouldThrowInvalidAmount_whenWithdrawAmountIsNegative() {
        // Act + Assert
        assertThrows(InvalidAmountException.class,
                () -> service.withdraw("FR001", new BigDecimal("-5.00")));
        verify(repository, never()).save(any());
    }

    @Test
    void shouldThrowInsufficientFunds_whenBalanceIsTooLow() {
        // Arrange
        Account existing = new Account("FR001", "Alice", new BigDecimal("30.00"));
        when(repository.findByNumber("FR001")).thenReturn(Optional.of(existing));

        // Act + Assert
        assertThrows(InsufficientFundsException.class,
                () -> service.withdraw("FR001", new BigDecimal("50.00")));
        verify(repository, never()).save(any());
    }

    @Test
    void shouldTransfer_whenAllConditionsAreMet() {
        // Arrange
        Account source = new Account("FR001", "Alice", new BigDecimal("300.00"));
        Account destination = new Account("FR002", "Bob", new BigDecimal("50.00"));
        Account updatedSource = new Account("FR001", "Alice", new BigDecimal("200.00"));
        Account updatedDestination = new Account("FR002", "Bob", new BigDecimal("150.00"));

        when(repository.findByNumber("FR001")).thenReturn(Optional.of(source));
        when(repository.findByNumber("FR002")).thenReturn(Optional.of(destination));
        when(repository.save(updatedSource)).thenReturn(updatedSource);
        when(repository.save(updatedDestination)).thenReturn(updatedDestination);

        // Act
        service.transfer("FR001", "FR002", new BigDecimal("100.00"));

        // Assert
        verify(repository).save(updatedSource);
        verify(repository).save(updatedDestination);
    }

    @Test
    void shouldThrowInvalidAmount_whenTransferAmountIsNull() {
        // Act + Assert
        assertThrows(InvalidAmountException.class, () -> service.transfer("FR001", "FR002", null));
        verify(repository, never()).save(any());
    }

    @Test
    void shouldThrowInvalidAmount_whenTransferAmountIsZero() {
        // Act + Assert
        assertThrows(InvalidAmountException.class,
                () -> service.transfer("FR001", "FR002", BigDecimal.ZERO));
        verify(repository, never()).save(any());
    }

    @Test
    void shouldThrowInvalidAmount_whenTransferAmountIsNegative() {
        // Act + Assert
        assertThrows(InvalidAmountException.class,
                () -> service.transfer("FR001", "FR002", new BigDecimal("-1.00")));
        verify(repository, never()).save(any());
    }

    @Test
    void shouldThrowInsufficientFunds_whenSourceBalanceIsTooLow() {
        // Arrange
        Account source = new Account("FR001", "Alice", new BigDecimal("20.00"));
        Account destination = new Account("FR002", "Bob", new BigDecimal("50.00"));
        when(repository.findByNumber("FR001")).thenReturn(Optional.of(source));
        when(repository.findByNumber("FR002")).thenReturn(Optional.of(destination));

        // Act + Assert
        assertThrows(InsufficientFundsException.class,
                () -> service.transfer("FR001", "FR002", new BigDecimal("100.00")));
        verify(repository, never()).save(any());
    }

    @Test
    void shouldThrowNotFound_whenDestinationAccountDoesNotExist() {
        // Arrange
        Account source = new Account("FR001", "Alice", new BigDecimal("200.00"));
        when(repository.findByNumber("FR001")).thenReturn(Optional.of(source));
        when(repository.findByNumber("FR999")).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(AccountNotFoundException.class,
                () -> service.transfer("FR001", "FR999", new BigDecimal("50.00")));
        verify(repository, never()).save(any());
    }

    @Test
    void shouldThrowNotFound_whenSourceAccountDoesNotExist() {
        // Arrange
        when(repository.findByNumber("FR999")).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(AccountNotFoundException.class,
                () -> service.transfer("FR999", "FR002", new BigDecimal("50.00")));
        verify(repository, never()).save(any());
    }
}
