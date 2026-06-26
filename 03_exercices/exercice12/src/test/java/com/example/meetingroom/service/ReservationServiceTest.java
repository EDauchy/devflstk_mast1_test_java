package com.example.meetingroom.service;

import com.example.meetingroom.exception.ReservationAlreadyCancelledException;
import com.example.meetingroom.exception.ReservationConflictException;
import com.example.meetingroom.exception.RoomNotFoundException;
import com.example.meetingroom.model.Reservation;
import com.example.meetingroom.model.ReservationStatus;
import com.example.meetingroom.model.Room;
import com.example.meetingroom.repository.ReservationRepository;
import com.example.meetingroom.repository.RoomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    private static final LocalDateTime START = LocalDateTime.of(2026, 6, 25, 10, 0);
    private static final LocalDateTime END = LocalDateTime.of(2026, 6, 25, 11, 0);

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private ReservationService service;

    @Test
    void shouldCreateReservation_whenDataIsValid() {
        // Arrange
        when(roomRepository.findById(1L)).thenReturn(Optional.of(new Room(1L, "Salle A", 10)));
        when(reservationRepository.findConfirmedByRoomId(1L)).thenReturn(List.of());
        when(reservationRepository.save(any())).thenAnswer(invocation -> {
            Reservation reservation = invocation.getArgument(0);
            return new Reservation(1L, reservation.roomId(), reservation.reservedBy(),
                    reservation.startTime(), reservation.endTime(), reservation.status());
        });

        // Act
        Reservation result = service.create(1L, "Alice", START, END);

        // Assert
        assertEquals(1L, result.id());
        assertEquals("Alice", result.reservedBy());
        assertEquals(ReservationStatus.CONFIRMED, result.status());
        verify(reservationRepository).save(any());
    }

    @Test
    void shouldThrowNotFound_whenRoomDoesNotExist() {
        // Arrange
        when(roomRepository.findById(99L)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(RoomNotFoundException.class,
                () -> service.create(99L, "Alice", START, END));
        verify(reservationRepository, never()).save(any());
    }

    @Test
    void shouldThrowBadRequest_whenTimeSlotIsInvalid() {
        // Arrange
        when(roomRepository.findById(1L)).thenReturn(Optional.of(new Room(1L, "Salle A", 10)));

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> service.create(1L, "Alice", END, START));
        verify(reservationRepository, never()).save(any());
    }

    @Test
    void shouldThrowConflict_whenTimeSlotOverlapsExistingReservation() {
        // Arrange
        Reservation existing = new Reservation(1L, 1L, "Bob", START, END, ReservationStatus.CONFIRMED);
        when(roomRepository.findById(1L)).thenReturn(Optional.of(new Room(1L, "Salle A", 10)));
        when(reservationRepository.findConfirmedByRoomId(1L)).thenReturn(List.of(existing));

        LocalDateTime overlappingStart = LocalDateTime.of(2026, 6, 25, 10, 30);
        LocalDateTime overlappingEnd = LocalDateTime.of(2026, 6, 25, 11, 30);

        // Act + Assert
        assertThrows(ReservationConflictException.class,
                () -> service.create(1L, "Alice", overlappingStart, overlappingEnd));
        verify(reservationRepository, never()).save(any());
    }

    @Test
    void shouldCancelReservation_whenReservationIsConfirmed() {
        // Arrange
        Reservation existing = new Reservation(1L, 1L, "Alice", START, END, ReservationStatus.CONFIRMED);
        Reservation cancelled = new Reservation(1L, 1L, "Alice", START, END, ReservationStatus.CANCELLED);
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(reservationRepository.save(cancelled)).thenReturn(cancelled);

        // Act
        Reservation result = service.cancel(1L);

        // Assert
        assertEquals(ReservationStatus.CANCELLED, result.status());
        verify(reservationRepository).save(cancelled);
    }

    @Test
    void shouldThrowConflict_whenReservationIsAlreadyCancelled() {
        // Arrange
        Reservation existing = new Reservation(1L, 1L, "Alice", START, END, ReservationStatus.CANCELLED);
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(existing));

        // Act + Assert
        assertThrows(ReservationAlreadyCancelledException.class, () -> service.cancel(1L));
        verify(reservationRepository, never()).save(any());
    }
}
