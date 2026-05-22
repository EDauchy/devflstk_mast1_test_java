package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EmailValidatorCsvSourceTest {
    private EmailValidator emailValidator;

    @BeforeEach
    void setUp() {
        emailValidator = new EmailValidator();
    }

    @ParameterizedTest(name = "{index} => email={0}, expected={1}")
    @CsvSource({
            "john.doe@example.com, true",
            "admin@test.fr, true",
            "invalid-email, false",
            "john.doe.example.com, false",
            "@example.com, false",
            "john.doe@, false"
    })
    @DisplayName("Doit valider plusieurs emails avec résultat attendu")
    void shouldValidateEmailWithExpectedResult(String email, boolean expectedResult) {
        boolean result = emailValidator.isValid(email);

        assertEquals(expectedResult, result);
    }
}
