package com.example.supportticket.service;

import com.example.supportticket.exception.InvalidStatusTransitionException;
import com.example.supportticket.exception.TicketNotFoundException;
import com.example.supportticket.model.Priority;
import com.example.supportticket.model.Ticket;
import com.example.supportticket.model.TicketStatus;
import com.example.supportticket.repository.TicketRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository repository;

    @InjectMocks
    private TicketService service;

    @Test
    void shouldCreateTicket_whenTitleAndPriorityAreValid() {
        // Arrange
        when(repository.save("Probleme reseau", Priority.HIGH))
                .thenReturn(new Ticket(1L, "Probleme reseau", Priority.HIGH, TicketStatus.OPEN));

        // Act
        Ticket result = service.create("Probleme reseau", Priority.HIGH);

        // Assert
        assertEquals(1L, result.id());
        assertEquals("Probleme reseau", result.title());
        assertEquals(Priority.HIGH, result.priority());
        verify(repository).save("Probleme reseau", Priority.HIGH);
    }

    @Test
    void shouldCreateTicketWithOpenStatus_whenTicketIsCreated() {
        // Arrange
        when(repository.save("Bug application", Priority.MEDIUM))
                .thenReturn(new Ticket(1L, "Bug application", Priority.MEDIUM, TicketStatus.OPEN));

        // Act
        Ticket result = service.create("Bug application", Priority.MEDIUM);

        // Assert
        assertEquals(TicketStatus.OPEN, result.status());
    }

    @Test
    void shouldReturnTicket_whenIdExists() {
        // Arrange
        Ticket ticket = new Ticket(1L, "Ticket existant", Priority.LOW, TicketStatus.OPEN);
        when(repository.findById(1L)).thenReturn(Optional.of(ticket));

        // Act
        Ticket result = service.getById(1L);

        // Assert
        assertEquals("Ticket existant", result.title());
        verify(repository).findById(1L);
    }

    @Test
    void shouldThrowNotFoundException_whenIdDoesNotExist() {
        // Arrange
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(TicketNotFoundException.class, () -> service.getById(99L));
        verify(repository).findById(99L);
    }

    @Test
    void shouldUpdateStatus_whenTransitionIsAllowed() {
        // Arrange
        Ticket existing = new Ticket(1L, "Ticket", Priority.HIGH, TicketStatus.OPEN);
        Ticket updated = new Ticket(1L, "Ticket", Priority.HIGH, TicketStatus.IN_PROGRESS);
        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.update(updated)).thenReturn(updated);

        // Act
        Ticket result = service.updateStatus(1L, TicketStatus.IN_PROGRESS);

        // Assert
        assertEquals(TicketStatus.IN_PROGRESS, result.status());
        verify(repository).update(updated);
    }

    @Test
    void shouldResolveTicket_whenTransitionFromOpenIsAllowed() {
        // Arrange
        Ticket existing = new Ticket(1L, "Ticket", Priority.MEDIUM, TicketStatus.OPEN);
        Ticket updated = new Ticket(1L, "Ticket", Priority.MEDIUM, TicketStatus.RESOLVED);
        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.update(updated)).thenReturn(updated);

        // Act
        Ticket result = service.updateStatus(1L, TicketStatus.RESOLVED);

        // Assert
        assertEquals(TicketStatus.RESOLVED, result.status());
    }

    @Test
    void shouldThrowConflictException_whenTransitionIsForbidden() {
        // Arrange
        Ticket existing = new Ticket(1L, "Ticket", Priority.LOW, TicketStatus.RESOLVED);
        when(repository.findById(1L)).thenReturn(Optional.of(existing));

        // Act + Assert
        assertThrows(InvalidStatusTransitionException.class,
                () -> service.updateStatus(1L, TicketStatus.IN_PROGRESS));
        verify(repository, never()).update(any());
    }

    @Test
    void shouldThrowConflictException_whenTransitionFromInProgressToOpenIsForbidden() {
        // Arrange
        Ticket existing = new Ticket(1L, "Ticket", Priority.LOW, TicketStatus.IN_PROGRESS);
        when(repository.findById(1L)).thenReturn(Optional.of(existing));

        // Act + Assert
        assertThrows(InvalidStatusTransitionException.class,
                () -> service.updateStatus(1L, TicketStatus.OPEN));
        verify(repository, never()).update(any());
    }

    @Test
    void shouldThrowException_whenTitleIsTooShort() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> service.create("ab", Priority.LOW));
        verify(repository, never()).save(any(), any());
    }

    @Test
    void shouldTrimTitle_whenCreatingTicket() {
        // Arrange
        when(repository.save("ABC", Priority.LOW))
                .thenReturn(new Ticket(1L, "ABC", Priority.LOW, TicketStatus.OPEN));

        // Act
        Ticket result = service.create("  ABC  ", Priority.LOW);

        // Assert
        assertEquals("ABC", result.title());
        verify(repository).save(eq("ABC"), eq(Priority.LOW));
    }
}
