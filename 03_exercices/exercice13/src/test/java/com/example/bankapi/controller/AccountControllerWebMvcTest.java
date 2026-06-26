package com.example.bankapi.controller;

import com.example.bankapi.exception.AccountNotFoundException;
import com.example.bankapi.exception.InsufficientFundsException;
import com.example.bankapi.model.Account;
import com.example.bankapi.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
class AccountControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AccountService service;

    @Test
    void shouldReturnCreated_whenAccountIsValid() throws Exception {
        // Arrange
        when(service.create("FR001", "Alice"))
                .thenReturn(new Account("FR001", "Alice", BigDecimal.ZERO));

        // Act + Assert
        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"number\":\"FR001\",\"holder\":\"Alice\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/accounts/FR001"))
                .andExpect(jsonPath("$.number").value("FR001"))
                .andExpect(jsonPath("$.balance").value(0));

        verify(service).create("FR001", "Alice");
    }

    @Test
    void shouldReturnNotFound_whenAccountDoesNotExist() throws Exception {
        // Arrange
        when(service.getByNumber("FR999")).thenThrow(new AccountNotFoundException("FR999"));

        // Act + Assert
        mockMvc.perform(get("/accounts/FR999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));

        verify(service).getByNumber("FR999");
    }

    @Test
    void shouldReturnConflict_whenWithdrawHasInsufficientFunds() throws Exception {
        // Arrange
        when(service.withdraw("FR001", new BigDecimal("100.00")))
                .thenThrow(new InsufficientFundsException("FR001", new BigDecimal("20.00"), new BigDecimal("100.00")));

        // Act + Assert
        mockMvc.perform(post("/accounts/FR001/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":100.00}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));

        verify(service).withdraw("FR001", new BigDecimal("100.00"));
    }

    @Test
    void shouldReturnBadRequest_whenDepositAmountIsInvalid() throws Exception {
        // Act + Assert
        mockMvc.perform(post("/accounts/FR001/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":0}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));

        verify(service, never()).deposit(any(), any());
    }
}
