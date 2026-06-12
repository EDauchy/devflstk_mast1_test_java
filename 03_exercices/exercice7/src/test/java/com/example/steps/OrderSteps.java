package com.example.steps;

import com.example.*;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderSteps {
    private ProductRepository productRepository;
    private CustomerRepository customerRepository;
    private OrderService orderService;
    private OrderResult orderResult;

    @Given("un produit {string} existe avec un prix de {double} et un stock de {int}")
    public void unProduitExiste(String reference, double price, int stock) {
        // Given
        initServices();
        Product product = new Product(reference, "Product " + reference, price, stock);
        when(productRepository.findByReference(reference)).thenReturn(Optional.of(product));
    }

    @Given("le produit {string} n'existe pas")
    public void leProduitNExistePas(String reference) {
        // Given
        initServices();
        when(productRepository.findByReference(reference)).thenReturn(Optional.empty());
    }

    @Given("le client {string} a le profil {word}")
    public void leClientALeProfil(String email, String profile) {
        // Given
        if (customerRepository == null) {
            initServices();
        }
        when(customerRepository.getProfileByEmail(email)).thenReturn(CustomerProfile.valueOf(profile));
    }

    @When("le client {string} commande {int} unités du produit {string}")
    @When("le client {string} commande {int} unité du produit {string}")
    public void leClientCommande(String email, int quantity, String reference) {
        // When
        orderResult = orderService.placeOrder(email, reference, quantity);
    }

    @Then("la commande est acceptée")
    public void laCommandeEstAcceptee() {
        // Then
        assertTrue(orderResult.isAccepted());
        assertNotNull(orderResult.getReceipt());
    }

    @Then("la commande est refusée")
    public void laCommandeEstRefusee() {
        // Then
        assertFalse(orderResult.isAccepted());
    }

    @Then("le montant total est de {double}")
    public void leMontantTotalEstDe(double expectedTotal) {
        // Then
        assertEquals(expectedTotal, orderResult.getReceipt().getTotalAmount(), 0.001);
    }

    @Then("le message de confirmation est {string}")
    public void leMessageDeConfirmationEst(String expectedMessage) {
        // Then
        assertEquals(expectedMessage, orderResult.getReceipt().getConfirmationMessage());
    }

    @Then("le motif de refus est {string}")
    public void leMotifDeRefusEst(String expectedReason) {
        // Then
        assertEquals(expectedReason, orderResult.getRejectionReason());
    }

    private void initServices() {
        productRepository = mock(ProductRepository.class);
        customerRepository = mock(CustomerRepository.class);
        orderService = new OrderService(productRepository, customerRepository);
    }
}
