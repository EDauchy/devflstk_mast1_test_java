package com.example;

import java.util.List;

public class BookingService {
    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;
    private final NotificationService notificationService;

    public BookingService(RoomRepository roomRepository,
                          ReservationRepository reservationRepository,
                          NotificationService notificationService) {
        this.roomRepository = roomRepository;
        this.reservationRepository = reservationRepository;
        this.notificationService = notificationService;
    }

    public BookingResult book(Reservation reservation) {
        var roomOptional = roomRepository.findByCode(reservation.getRoomCode());
        if (roomOptional.isEmpty()) {
            return BookingResult.rejected("Room not found");
        }

        Room room = roomOptional.get();

        if (reservation.getParticipants() > room.getMaxCapacity()) {
            return BookingResult.rejected("Capacity exceeded");
        }

        if (!reservation.getEndDate().isAfter(reservation.getStartDate())) {
            return BookingResult.rejected("Invalid period");
        }

        List<Reservation> existingReservations = reservationRepository.findByRoomCode(reservation.getRoomCode());
        for (Reservation existing : existingReservations) {
            if (overlaps(reservation, existing)) {
                return BookingResult.rejected("Room already booked");
            }
        }

        reservationRepository.save(reservation);
        notificationService.sendConfirmation(
                reservation.getUserEmail(),
                "Reservation confirmed for room " + reservation.getRoomCode()
        );

        return BookingResult.accepted();
    }

    private boolean overlaps(Reservation requested, Reservation existing) {
        return requested.getStartDate().isBefore(existing.getEndDate())
                && existing.getStartDate().isBefore(requested.getEndDate());
    }
}
