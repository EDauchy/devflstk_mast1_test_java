package com.example.meetingroom.service;

import com.example.meetingroom.exception.ReservationAlreadyCancelledException;
import com.example.meetingroom.exception.ReservationConflictException;
import com.example.meetingroom.exception.ReservationNotFoundException;
import com.example.meetingroom.exception.RoomNotFoundException;
import com.example.meetingroom.model.Reservation;
import com.example.meetingroom.model.ReservationStatus;
import com.example.meetingroom.repository.ReservationRepository;
import com.example.meetingroom.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;

    public ReservationService(ReservationRepository reservationRepository, RoomRepository roomRepository) {
        this.reservationRepository = reservationRepository;
        this.roomRepository = roomRepository;
    }

    public Reservation create(Long roomId, String reservedBy, LocalDateTime startTime, LocalDateTime endTime) {
        if (roomRepository.findById(roomId).isEmpty()) {
            throw new RoomNotFoundException(roomId);
        }

        if (reservedBy == null || reservedBy.isBlank()) {
            throw new IllegalArgumentException("Le nom de la personne qui réserve est obligatoire");
        }

        if (startTime == null || endTime == null) {
            throw new IllegalArgumentException("Les dates de début et de fin sont obligatoires");
        }

        if (!endTime.isAfter(startTime)) {
            throw new IllegalArgumentException("La date de fin doit être strictement après la date de début");
        }

        if (hasOverlap(roomId, startTime, endTime)) {
            throw new ReservationConflictException("Le créneau chevauche une réservation existante");
        }

        Reservation reservation = new Reservation(
                null,
                roomId,
                reservedBy.trim(),
                startTime,
                endTime,
                ReservationStatus.CONFIRMED
        );

        return reservationRepository.save(reservation);
    }

    public Reservation getById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException(id));
    }

    public Reservation cancel(Long id) {
        Reservation reservation = getById(id);

        if (reservation.status() == ReservationStatus.CANCELLED) {
            throw new ReservationAlreadyCancelledException(id);
        }

        Reservation cancelled = new Reservation(
                reservation.id(),
                reservation.roomId(),
                reservation.reservedBy(),
                reservation.startTime(),
                reservation.endTime(),
                ReservationStatus.CANCELLED
        );

        return reservationRepository.save(cancelled);
    }

    public void deleteAll() {
        reservationRepository.deleteAll();
    }

    boolean hasOverlap(Long roomId, LocalDateTime startTime, LocalDateTime endTime) {
        List<Reservation> confirmed = reservationRepository.findConfirmedByRoomId(roomId);

        return confirmed.stream()
                .anyMatch(existing -> overlaps(existing.startTime(), existing.endTime(), startTime, endTime));
    }

    boolean overlaps(LocalDateTime start1, LocalDateTime end1, LocalDateTime start2, LocalDateTime end2) {
        return start1.isBefore(end2) && start2.isBefore(end1);
    }
}
