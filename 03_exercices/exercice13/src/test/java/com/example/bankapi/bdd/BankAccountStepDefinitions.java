package com.example.bankapi.bdd;

import com.example.bankapi.repository.AccountRepository;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BankAccountStepDefinitions {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository repository;

    private ResultActions lastResult;

    @Given("aucun compte n existe dans l API")
    public void noAccountExists() {
        repository.deleteAll();
        lastResult = null;
    }

    @Given("un compte {string} existe pour {string}")
    public void accountExists(String number, String holder) throws Exception {
        createAccount(number, holder);
    }

    @Given("un compte {string} existe pour {string} avec un solde de {int} euros")
    public void accountExistsWithBalance(String number, String holder, int balance) throws Exception {
        createAccount(number, holder);
        mockMvc.perform(post("/accounts/" + number + "/deposit")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\":" + balance + "}"));
    }

    @When("je cree un compte avec le numero {string} pour le titulaire {string}")
    public void createAccountStep(String number, String holder) throws Exception {
        lastResult = createAccount(number, holder);
    }

    @When("je depose {int} euros sur le compte {string}")
    public void deposit(int amount, String number) throws Exception {
        lastResult = mockMvc.perform(post("/accounts/" + number + "/deposit")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\":" + amount + "}"));
    }

    @When("je retire {int} euros du compte {string}")
    public void withdraw(int amount, String number) throws Exception {
        lastResult = mockMvc.perform(post("/accounts/" + number + "/withdraw")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\":" + amount + "}"));
    }

    @When("je vire {int} euros du compte {string} vers le compte {string}")
    public void transfer(int amount, String fromAccount, String toAccount) throws Exception {
        lastResult = mockMvc.perform(post("/accounts/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "fromAccount": "%s",
                          "toAccount": "%s",
                          "amount": %d
                        }
                        """.formatted(fromAccount, toAccount, amount)));
    }

    @Then("la reponse HTTP doit etre {int}")
    public void responseStatusShouldBe(int expectedStatus) throws Exception {
        lastResult.andExpect(status().is(expectedStatus));
    }

    @Then("la reponse contient le solde {int} pour le compte {string}")
    public void responseShouldContainBalance(int expectedBalance, String number) throws Exception {
        lastResult.andExpect(jsonPath("$.number").value(number))
                .andExpect(jsonPath("$.balance").value(expectedBalance));
    }

    @Then("le compte {string} a un solde de {int} euros")
    public void accountShouldHaveBalance(String number, int expectedBalance) throws Exception {
        mockMvc.perform(get("/accounts/" + number))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(expectedBalance));
    }

    @Then("la reponse contient un message d erreur")
    public void responseShouldContainErrorMessage() throws Exception {
        lastResult.andExpect(jsonPath("$.message").exists());
    }

    private ResultActions createAccount(String number, String holder) throws Exception {
        return mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"number\":\"" + number + "\",\"holder\":\"" + holder + "\"}"));
    }
}
