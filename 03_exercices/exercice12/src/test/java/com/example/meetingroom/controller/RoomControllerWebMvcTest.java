package com.example.meetingroom.controller;

import com.example.meetingroom.exception.ReservationConflictException;
import com.example.meetingroom.exception.ReservationNotFoundException;
import com.example.meetingroom.model.ReservationStatus;
import com.example.meetingroom.model.Room;
import com.example.meetingroom.service.RoomService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RoomController.class)
class RoomControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RoomService service;

    @Test
    void shouldReturnCreated_whenRoomIsValid() throws Exception {
        // Arrange
        when(service.create("Salle Atlas", 12)).thenReturn(new Room(1L, "Salle Atlas", 12));

        // Act + Assert
        mockMvc.perform(post("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Salle Atlas\",\"capacity\":12}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/rooms/1"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Salle Atlas"))
                .andExpect(jsonPath("$.capacity").value(12));

        verify(service).create("Salle Atlas", 12);
    }

    @Test
    void shouldReturnBadRequest_whenRoomIsInvalid() throws Exception {
        // Act + Assert
        mockMvc.perform(post("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\",\"capacity\":0}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));

        verify(service, never()).create(anyString(), anyInt());
    }
}
