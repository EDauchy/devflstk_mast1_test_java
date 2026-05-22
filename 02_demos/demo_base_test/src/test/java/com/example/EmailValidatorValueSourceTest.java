package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class EmailValidatorValueSourceTest {

    private EmailValidator emailValidator;

    @BeforeEach
    void setUp() {
        emailValidator = new EmailValidator();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "john.doe@example.com",
            "admin@test.fr",
            "contact@company.org"
    })
    @DisplayName("Doit accepter plusieurs emails valides")
    void shouldReturnTrueWhenEmailsAreValid(String email) {
        boolean result = emailValidator.isValid(email);

        assertTrue(result);
    }



}
