package com.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PriceCalculatorTest {
    @Test
    void shouldCalculateTotalPrice() {
        // Arrange
        PriceCalculator priceCalculator = new PriceCalculator();

        // Act
        double result = priceCalculator.calculateTotalPrice(10.0, 3);

        // Assert
        assertEquals(30.0, result);
    }

    @Test
    void shouldApplyDiscount() {
        // Arrange
        PriceCalculator priceCalculator = new PriceCalculator();

        // Act
        double result = priceCalculator.applyDiscount(100.0, 0.20);

        // Assert
        assertEquals(80.0, result);
    }

    @Test
    void shouldCalculateVat() {
        // Arrange
        PriceCalculator priceCalculator = new PriceCalculator();

        // Act
        double result = priceCalculator.calculateVat(100.0, 0.20);

        // Assert
        assertEquals(20.0, result);
    }

    @Test
    void shouldCalculatePriceWithVat() {
        // Arrange
        PriceCalculator priceCalculator = new PriceCalculator();

        // Act
        double result = priceCalculator.calculatePriceWithVat(100.0, 0.20);

        // Assert
        assertEquals(120.0, result);
    }

    @Test
    void shouldThrowExceptionWhenUnitPriceIsNegative() {
        // Arrange
        PriceCalculator priceCalculator = new PriceCalculator();

        // Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> priceCalculator.calculateTotalPrice(-10.0, 3)
        );

        // Assert
        assertEquals("Unit price must not be negative", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenQuantityIsNegative() {
        // Arrange
        PriceCalculator priceCalculator = new PriceCalculator();

        // Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> priceCalculator.calculateTotalPrice(10.0, -3)
        );

        // Assert
        assertEquals("Quantity must not be negative", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenDiscountRateIsNegative() {
        // Arrange
        PriceCalculator priceCalculator = new PriceCalculator();

        // Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> priceCalculator.applyDiscount(100.0, -0.20)
        );

        // Assert
        assertEquals("Discount rate must not be negative", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenVatRateIsNegative() {
        // Arrange
        PriceCalculator priceCalculator = new PriceCalculator();

        // Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> priceCalculator.calculateVat(100.0, -0.20)
        );

        // Assert
        assertEquals("VAT rate must not be negative", exception.getMessage());
    }
}
