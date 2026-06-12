package com.example;

import java.util.Optional;

public interface RoomRepository {
    Optional<Room> findByCode(String code);
}
