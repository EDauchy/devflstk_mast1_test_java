package com.example.supportticket.bdd;

import com.example.supportticket.repository.TicketRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SupportTicketStepDefinitions {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TicketRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    private ResultActions lastResult;
    private Long createdTicketId;

    @Given("aucun ticket n existe dans l API")
    public void noTicketExists() {
        repository.deleteAll();
        createdTicketId = null;
    }

    @Given("un ticket existe avec le titre {string} et la priorite {string}")
    public void ticketExists(String title, String priority) throws Exception {
        createTicket(title, priority);
    }

    @Given("un ticket resolu existe avec le titre {string}")
    public void resolvedTicketExists(String title) throws Exception {
        createTicket(title, "MEDIUM");
        mockMvc.perform(patch("/api/tickets/" + createdTicketId + "/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"status\":\"RESOLVED\"}"));
    }

    @When("je cree un ticket avec le titre {string} et la priorite {string}")
    public void createTicketStep(String title, String priority) throws Exception {
        createTicket(title, priority);
    }

    @When("je resous le ticket cree")
    public void resolveCreatedTicket() throws Exception {
        lastResult = mockMvc.perform(patch("/api/tickets/" + createdTicketId + "/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"status\":\"RESOLVED\"}"));
    }

    @When("je tente de passer le ticket {long} au statut {string}")
    public void updateTicketStatus(Long id, String status) throws Exception {
        lastResult = mockMvc.perform(patch("/api/tickets/" + id + "/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"status\":\"" + status + "\"}"));
    }

    @When("je demande le ticket avec l identifiant {long}")
    public void getTicketById(Long id) throws Exception {
        lastResult = mockMvc.perform(get("/api/tickets/" + id));
    }

    @Then("la reponse HTTP doit etre {int}")
    public void responseStatusShouldBe(int expectedStatus) throws Exception {
        lastResult.andExpect(status().is(expectedStatus));
    }

    @Then("la reponse contient le titre {string}")
    public void responseShouldContainTitle(String expectedTitle) throws Exception {
        lastResult.andExpect(jsonPath("$.title").value(expectedTitle));
    }

    @Then("la reponse contient le statut {string}")
    public void responseShouldContainStatus(String expectedStatus) throws Exception {
        lastResult.andExpect(jsonPath("$.status").value(expectedStatus));
    }

    @Then("la reponse contient un message d erreur")
    public void responseShouldContainErrorMessage() throws Exception {
        lastResult.andExpect(jsonPath("$.message").exists());
    }

    private void createTicket(String title, String priority) throws Exception {
        lastResult = mockMvc.perform(post("/api/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"" + title + "\",\"priority\":\"" + priority + "\"}"));

        var responseBody = lastResult.andReturn().getResponse().getContentAsString();
        if (!responseBody.isBlank() && lastResult.andReturn().getResponse().getStatus() == 201) {
            createdTicketId = objectMapper.readTree(responseBody).get("id").asLong();
        }
    }
}
