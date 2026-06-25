package com.example.supportticket.repository;

import com.example.supportticket.model.Priority;
import com.example.supportticket.model.Ticket;

import java.util.List;
import java.util.Optional;

public interface TicketRepository {

    Ticket save(String title, Priority priority);

    Optional<Ticket> findById(Long id);

    List<Ticket> findAll();

    Ticket update(Ticket ticket);

    void deleteAll();
}
