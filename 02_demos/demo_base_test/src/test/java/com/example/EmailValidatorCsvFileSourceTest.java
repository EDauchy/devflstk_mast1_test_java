package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EmailValidatorCsvFileSourceTest {
    private EmailValidator emailValidator;

    @BeforeEach
    void setUp() {
        emailValidator = new EmailValidator();
    }

    @ParameterizedTest(name = "{index} => email={0}, expected={1}")
    @CsvFileSource(resources = "/email-cases.csv", numLinesToSkip = 1)
    @DisplayName("Doit valider les emails depuis un fichier CSV")
    void shouldValidateEmailUsingCsvFile(String email, boolean expectedResult) {
        boolean result = emailValidator.isValid(email);

        assertEquals(expectedResult, result);
    }
}
