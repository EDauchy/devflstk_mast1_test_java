package com.example.supportticket.repository;

import com.example.supportticket.model.Priority;
import com.example.supportticket.model.Ticket;
import com.example.supportticket.model.TicketStatus;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryTicketRepository implements TicketRepository {

    private final AtomicLong sequence = new AtomicLong(0);
    private final Map<Long, Ticket> tickets = new ConcurrentHashMap<>();

    @Override
    public Ticket save(String title, Priority priority) {
        Long id = sequence.incrementAndGet();
        Ticket ticket = new Ticket(id, title, priority, TicketStatus.OPEN);
        tickets.put(id, ticket);
        return ticket;
    }

    @Override
    public Optional<Ticket> findById(Long id) {
        return Optional.ofNullable(tickets.get(id));
    }

    @Override
    public List<Ticket> findAll() {
        return new ArrayList<>(tickets.values())
                .stream()
                .sorted(Comparator.comparing(Ticket::id))
                .toList();
    }

    @Override
    public Ticket update(Ticket ticket) {
        tickets.put(ticket.id(), ticket);
        return ticket;
    }

    @Override
    public void deleteAll() {
        tickets.clear();
        sequence.set(0);
    }
}
