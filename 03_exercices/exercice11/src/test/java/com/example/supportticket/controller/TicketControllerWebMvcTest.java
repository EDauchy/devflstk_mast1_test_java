package com.example.supportticket.controller;

import com.example.supportticket.exception.InvalidStatusTransitionException;
import com.example.supportticket.exception.TicketNotFoundException;
import com.example.supportticket.model.Priority;
import com.example.supportticket.model.Ticket;
import com.example.supportticket.model.TicketStatus;
import com.example.supportticket.service.TicketService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TicketController.class)
class TicketControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TicketService service;

    @Test
    void shouldReturnCreated_whenPostBodyIsValid() throws Exception {
        // Arrange
        when(service.create("Probleme VPN", Priority.HIGH))
                .thenReturn(new Ticket(1L, "Probleme VPN", Priority.HIGH, TicketStatus.OPEN));

        // Act + Assert
        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Probleme VPN\",\"priority\":\"HIGH\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/tickets/1"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Probleme VPN"))
                .andExpect(jsonPath("$.priority").value("HIGH"))
                .andExpect(jsonPath("$.status").value("OPEN"));

        verify(service).create("Probleme VPN", Priority.HIGH);
    }

    @Test
    void shouldReturnBadRequest_whenPostBodyIsInvalid() throws Exception {
        // Act + Assert
        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"ab\",\"priority\":\"HIGH\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));

        verify(service, never()).create(any(), any());
    }

    @Test
    void shouldReturnOk_whenTicketExists() throws Exception {
        // Arrange
        when(service.getById(1L))
                .thenReturn(new Ticket(1L, "Ticket", Priority.MEDIUM, TicketStatus.OPEN));

        // Act + Assert
        mockMvc.perform(get("/api/tickets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("OPEN"));

        verify(service).getById(1L);
    }

    @Test
    void shouldReturnNotFound_whenTicketDoesNotExist() throws Exception {
        // Arrange
        when(service.getById(99L)).thenThrow(new TicketNotFoundException(99L));

        // Act + Assert
        mockMvc.perform(get("/api/tickets/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Aucun ticket trouvé avec l'identifiant 99"));

        verify(service).getById(99L);
    }

    @Test
    void shouldReturnOk_whenStatusUpdateIsValid() throws Exception {
        // Arrange
        when(service.updateStatus(1L, TicketStatus.RESOLVED))
                .thenReturn(new Ticket(1L, "Ticket", Priority.LOW, TicketStatus.RESOLVED));

        // Act + Assert
        mockMvc.perform(patch("/api/tickets/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"RESOLVED\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("RESOLVED"));

        verify(service).updateStatus(1L, TicketStatus.RESOLVED);
    }

    @Test
    void shouldReturnConflict_whenStatusTransitionIsForbidden() throws Exception {
        // Arrange
        when(service.updateStatus(1L, TicketStatus.IN_PROGRESS))
                .thenThrow(new InvalidStatusTransitionException(TicketStatus.RESOLVED, TicketStatus.IN_PROGRESS));

        // Act + Assert
        mockMvc.perform(patch("/api/tickets/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"IN_PROGRESS\"}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));

        verify(service).updateStatus(eq(1L), eq(TicketStatus.IN_PROGRESS));
    }
}
