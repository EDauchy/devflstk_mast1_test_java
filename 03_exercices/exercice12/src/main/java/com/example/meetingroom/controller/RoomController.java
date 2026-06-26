package com.example.meetingroom.controller;

import com.example.meetingroom.dto.CreateRoomRequest;
import com.example.meetingroom.dto.RoomResponse;
import com.example.meetingroom.service.RoomService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService service;

    public RoomController(RoomService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<RoomResponse> create(@Valid @RequestBody CreateRoomRequest request) {
        var created = service.create(request.name(), request.capacity());
        var response = RoomResponse.from(created);

        return ResponseEntity
                .created(URI.create("/api/rooms/" + response.id()))
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<RoomResponse>> findAll() {
        var responses = service.findAll()
                .stream()
                .map(RoomResponse::from)
                .toList();

        return ResponseEntity.ok(responses);
    }
}
