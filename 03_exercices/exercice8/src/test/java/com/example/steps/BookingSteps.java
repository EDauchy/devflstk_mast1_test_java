package com.example.steps;

import com.example.*;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookingSteps {
    private RoomRepository roomRepository;
    private ReservationRepository reservationRepository;
    private NotificationService notificationService;
    private BookingService bookingService;
    private BookingResult bookingResult;
    private final List<Reservation> existingReservations = new ArrayList<>();
    private String lastUserEmail;

    @Given("la salle {string} existe avec une capacité maximale de {int}")
    public void laSalleExiste(String code, int capacity) {
        // Given
        initServices();
        Room room = new Room(code, "Room " + code, capacity);
        when(roomRepository.findByCode(code)).thenReturn(Optional.of(room));
    }

    @Given("la salle {string} n'existe pas")
    public void laSalleNExistePas(String code) {
        // Given
        initServices();
        when(roomRepository.findByCode(code)).thenReturn(Optional.empty());
    }

    @Given("aucune réservation n'existe pour la salle {string}")
    public void aucuneReservationPourLaSalle(String roomCode) {
        // Given
        if (reservationRepository == null) {
            initServices();
        }
        when(reservationRepository.findByRoomCode(roomCode)).thenReturn(List.of());
    }

    @Given("une réservation existante pour la salle {string} de {string} à {string}")
    public void uneReservationExistante(String roomCode, String start, String end) {
        // Given
        if (reservationRepository == null) {
            initServices();
        }
        Reservation existing = new Reservation(
                "existing@example.com",
                roomCode,
                4,
                LocalDateTime.parse(start),
                LocalDateTime.parse(end)
        );
        existingReservations.add(existing);
        when(reservationRepository.findByRoomCode(roomCode)).thenReturn(new ArrayList<>(existingReservations));
    }

    @When("l'utilisateur {string} réserve la salle {string} pour {int} participants de {string} à {string}")
    public void lUtilisateurReserve(String email, String roomCode, int participants, String start, String end) {
        // When
        lastUserEmail = email;
        Reservation reservation = new Reservation(
                email,
                roomCode,
                participants,
                LocalDateTime.parse(start),
                LocalDateTime.parse(end)
        );
        bookingResult = bookingService.book(reservation);
    }

    @Then("la réservation est acceptée")
    public void laReservationEstAcceptee() {
        // Then
        assertTrue(bookingResult.isAccepted());
    }

    @Then("la réservation est refusée")
    public void laReservationEstRefusee() {
        // Then
        assertFalse(bookingResult.isAccepted());
    }

    @Then("le motif de refus est {string}")
    public void leMotifDeRefusEst(String expectedReason) {
        // Then
        assertEquals(expectedReason, bookingResult.getRejectionReason());
    }

    @Then("une notification de confirmation est envoyée à {string}")
    public void uneNotificationEstEnvoyee(String email) {
        // Then
        verify(notificationService).sendConfirmation(eq(email), contains("Reservation confirmed"));
    }

    @Then("aucune notification n'est envoyée à {string}")
    public void aucuneNotificationNestEnvoyee(String email) {
        // Then
        verify(notificationService, never()).sendConfirmation(eq(email), anyString());
    }

    private void initServices() {
        roomRepository = mock(RoomRepository.class);
        reservationRepository = mock(ReservationRepository.class);
        notificationService = mock(NotificationService.class);
        bookingService = new BookingService(roomRepository, reservationRepository, notificationService);
        existingReservations.clear();
    }
}
