package com.example.meetingroom.controller;

import com.example.meetingroom.exception.ReservationConflictException;
import com.example.meetingroom.exception.ReservationNotFoundException;
import com.example.meetingroom.model.Reservation;
import com.example.meetingroom.model.ReservationStatus;
import com.example.meetingroom.service.ReservationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReservationController.class)
class ReservationControllerWebMvcTest {

    private static final LocalDateTime START = LocalDateTime.of(2026, 6, 25, 10, 0);
    private static final LocalDateTime END = LocalDateTime.of(2026, 6, 25, 11, 0);

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReservationService service;

    @Test
    void shouldReturnCreated_whenReservationIsValid() throws Exception {
        // Arrange
        Reservation reservation = new Reservation(1L, 1L, "Alice", START, END, ReservationStatus.CONFIRMED);
        when(service.create(1L, "Alice", START, END)).thenReturn(reservation);

        // Act + Assert
        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "roomId": 1,
                                  "reservedBy": "Alice",
                                  "startTime": "2026-06-25T10:00:00",
                                  "endTime": "2026-06-25T11:00:00"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("CONFIRMED"));

        verify(service).create(1L, "Alice", START, END);
    }

    @Test
    void shouldReturnNotFound_whenReservationDoesNotExist() throws Exception {
        // Arrange
        when(service.getById(99L)).thenThrow(new ReservationNotFoundException(99L));

        // Act + Assert
        mockMvc.perform(get("/api/reservations/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));

        verify(service).getById(99L);
    }

    @Test
    void shouldReturnConflict_whenBusinessRulePreventsReservation() throws Exception {
        // Arrange
        when(service.create(1L, "Alice", START, END))
                .thenThrow(new ReservationConflictException("Le créneau chevauche une réservation existante"));

        // Act + Assert
        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "roomId": 1,
                                  "reservedBy": "Alice",
                                  "startTime": "2026-06-25T10:00:00",
                                  "endTime": "2026-06-25T11:00:00"
                                }
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));

        verify(service).create(1L, "Alice", START, END);
    }
}
