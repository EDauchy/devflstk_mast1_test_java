package com.example.meetingroom.repository;

import com.example.meetingroom.model.Room;

import java.util.List;
import java.util.Optional;

public interface RoomRepository {

    Room save(String name, int capacity);

    Optional<Room> findById(Long id);

    List<Room> findAll();

    void deleteAll();
}
