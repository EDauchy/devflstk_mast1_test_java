package com.example;

public class Product {
    private final String reference;
    private final String name;
    private final double unitPrice;
    private final int stock;

    public Product(String reference, String name, double unitPrice, int stock) {
        this.reference = reference;
        this.name = name;
        this.unitPrice = unitPrice;
        this.stock = stock;
    }

    public String getReference() {
        return reference;
    }

    public String getName() {
        return name;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public int getStock() {
        return stock;
    }
}
