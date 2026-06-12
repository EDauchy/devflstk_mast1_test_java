package com.example;

import java.util.List;

public interface ReservationRepository {
    List<Reservation> findByRoomCode(String roomCode);

    void save(Reservation reservation);
}
