package com.example.bankapi.model;

import java.math.BigDecimal;

public record Account(String number, String holder, BigDecimal balance) {
}
