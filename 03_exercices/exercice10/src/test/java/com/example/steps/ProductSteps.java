package com.example.steps;

import com.example.Product;
import com.example.ProductRepository;
import com.example.ProductService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ProductSteps {
    private ProductRepository productRepository;
    private ProductService productService;
    private List<Product> searchResults;

    @Given("the search bar is available")
    public void theSearchBarIsAvailable() {
        // Given
        productRepository = mock(ProductRepository.class);
        productService = new ProductService(productRepository);
    }

    @Given("the categories page is available")
    public void theCategoriesPageIsAvailable() {
        // Given
        productRepository = mock(ProductRepository.class);
        productService = new ProductService(productRepository);
    }

    @Given("the product repository returns products for keyword {string}")
    public void theProductRepositoryReturnsProductsForKeyword(String keyword) {
        // Given
        when(productRepository.findByKeyword(keyword)).thenReturn(List.of(
                new Product("P1", "Smartphone", "Electronics", 299.0),
                new Product("P2", "Phone Case", "Accessories", 19.0)
        ));
    }

    @Given("the product repository returns products with max price {double}")
    public void theProductRepositoryReturnsProductsWithMaxPrice(double maxPrice) {
        // Given
        when(productRepository.findByMaxPrice(maxPrice)).thenReturn(List.of(
                new Product("P3", "USB Cable", "Electronics", 9.0)
        ));
    }

    @Given("the product repository returns products for category {string}")
    public void theProductRepositoryReturnsProductsForCategory(String category) {
        // Given
        when(productRepository.findByCategory(category)).thenReturn(List.of(
                new Product("P4", "Laptop", "Electronics", 999.0),
                new Product("P5", "Headphones", "Electronics", 149.0)
        ));
    }

    @When("the user searches for keyword {string}")
    public void theUserSearchesForKeyword(String keyword) {
        // When
        searchResults = productService.searchByKeyword(keyword);
    }

    @When("the user searches for products with max price {double}")
    public void theUserSearchesForProductsWithMaxPrice(double maxPrice) {
        // When
        searchResults = productService.searchByMaxPrice(maxPrice);
    }

    @When("the user selects category {string}")
    public void theUserSelectsCategory(String category) {
        // When
        searchResults = productService.getProductsByCategory(category);
    }

    @Then("the search results should contain {int} products")
    public void theSearchResultsShouldContainProducts(int count) {
        // Then
        assertEquals(count, searchResults.size());
    }

    @Then("the category results should contain {int} products")
    public void theCategoryResultsShouldContainProducts(int count) {
        // Then
        assertEquals(count, searchResults.size());
    }

    @Then("the product repository should have been queried with keyword {string}")
    public void theProductRepositoryShouldHaveBeenQueriedWithKeyword(String keyword) {
        // Then
        verify(productRepository).findByKeyword(keyword);
    }

    @Then("the product repository should have been queried with max price {double}")
    public void theProductRepositoryShouldHaveBeenQueriedWithMaxPrice(double maxPrice) {
        // Then
        verify(productRepository).findByMaxPrice(maxPrice);
    }

    @Then("the product repository should have been queried with category {string}")
    public void theProductRepositoryShouldHaveBeenQueriedWithCategory(String category) {
        // Then
        verify(productRepository).findByCategory(category);
    }
}
