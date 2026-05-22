package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EmailValidatorMethodSourceTest {

    private EmailValidator emailValidator;

    @BeforeEach
    void setUp() {
        emailValidator = new EmailValidator();
    }

    static Stream<Object[]> emailCases() {
        return Stream.of(
                new Object[]{"john.doe@example.com", true},
                new Object[]{"admin@test.fr", true},
                new Object[]{"invalid-email", false},
                new Object[]{"john.doe.example.com", false},
                new Object[]{"@example.com", false},
                new Object[]{"john.doe@", false}
        );
    }

    @ParameterizedTest(name = "{index} => email={0}, expected={1}")
    @MethodSource("emailCases")
    @DisplayName("Doit valider les emails depuis une méthode source")
    void shouldValidateEmailUsingMethodSource(String email, boolean expectedResult) {
        boolean result = emailValidator.isValid(email);

        assertEquals(expectedResult, result);
    }
}
