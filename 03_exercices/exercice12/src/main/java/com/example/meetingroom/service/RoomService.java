package com.example.meetingroom.service;

import com.example.meetingroom.exception.RoomNotFoundException;
import com.example.meetingroom.model.Room;
import com.example.meetingroom.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    private final RoomRepository repository;

    public RoomService(RoomRepository repository) {
        this.repository = repository;
    }

    public Room create(String name, int capacity) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Le nom est obligatoire");
        }

        if (capacity < 1) {
            throw new IllegalArgumentException("La capacité doit être supérieure ou égale à 1");
        }

        return repository.save(name.trim(), capacity);
    }

    public Room getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RoomNotFoundException(id));
    }

    public List<Room> findAll() {
        return repository.findAll();
    }

    public void deleteAll() {
        repository.deleteAll();
    }
}
