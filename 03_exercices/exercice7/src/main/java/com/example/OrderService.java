package com.example;

public class OrderService {
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    public OrderService(ProductRepository productRepository, CustomerRepository customerRepository) {
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
    }

    public OrderResult placeOrder(String customerEmail, String productReference, int quantity) {
        var productOptional = productRepository.findByReference(productReference);
        if (productOptional.isEmpty()) {
            return OrderResult.rejected("Product not found");
        }

        Product product = productOptional.get();
        if (quantity > product.getStock()) {
            return OrderResult.rejected("Insufficient stock");
        }

        CustomerProfile profile = customerRepository.getProfileByEmail(customerEmail);
        double baseAmount = product.getUnitPrice() * quantity;
        double totalAmount = baseAmount * (1 - profile.getDiscountRate());

        OrderReceipt receipt = new OrderReceipt(
                productReference,
                quantity,
                totalAmount,
                "Order confirmed for " + customerEmail
        );

        return OrderResult.accepted(receipt);
    }
}
