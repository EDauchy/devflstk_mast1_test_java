package com.example.meetingroom.bdd;

import com.example.meetingroom.repository.ReservationRepository;
import com.example.meetingroom.repository.RoomRepository;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MeetingRoomStepDefinitions {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    private ResultActions lastResult;

    @Given("aucune donnee n existe dans l API")
    public void noDataExists() {
        reservationRepository.deleteAll();
        roomRepository.deleteAll();
    }

    @Given("une salle {string} avec une capacite de {int} existe")
    public void roomExists(String name, int capacity) throws Exception {
        mockMvc.perform(post("/api/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"" + name + "\",\"capacity\":" + capacity + "}"));
    }

    @Given("une reservation confirmee existe pour la salle {long} de {string} a {string}")
    public void confirmedReservationExists(Long roomId, String startTime, String endTime) throws Exception {
        mockMvc.perform(post("/api/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "roomId": %d,
                          "reservedBy": "Alice",
                          "startTime": "%s",
                          "endTime": "%s"
                        }
                        """.formatted(roomId, startTime, endTime)));
    }

    @When("je reserve la salle {long} pour {string} de {string} a {string}")
    public void createReservation(Long roomId, String reservedBy, String startTime, String endTime) throws Exception {
        lastResult = mockMvc.perform(post("/api/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "roomId": %d,
                          "reservedBy": "%s",
                          "startTime": "%s",
                          "endTime": "%s"
                        }
                        """.formatted(roomId, reservedBy, startTime, endTime)));
    }

    @Then("la reponse HTTP doit etre {int}")
    public void responseStatusShouldBe(int expectedStatus) throws Exception {
        lastResult.andExpect(status().is(expectedStatus));
    }

    @Then("la reponse contient le statut de reservation {string}")
    public void responseShouldContainReservationStatus(String expectedStatus) throws Exception {
        lastResult.andExpect(jsonPath("$.status").value(expectedStatus));
    }

    @Then("la reponse contient un message d erreur")
    public void responseShouldContainErrorMessage() throws Exception {
        lastResult.andExpect(jsonPath("$.message").exists());
    }
}
