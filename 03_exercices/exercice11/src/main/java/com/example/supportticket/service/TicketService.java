package com.example.supportticket.service;

import com.example.supportticket.exception.InvalidStatusTransitionException;
import com.example.supportticket.exception.TicketNotFoundException;
import com.example.supportticket.model.Priority;
import com.example.supportticket.model.Ticket;
import com.example.supportticket.model.TicketStatus;
import com.example.supportticket.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {

    private final TicketRepository repository;

    public TicketService(TicketRepository repository) {
        this.repository = repository;
    }

    public Ticket create(String title, Priority priority) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Le titre est obligatoire");
        }

        String trimmedTitle = title.trim();
        if (trimmedTitle.length() < 3) {
            throw new IllegalArgumentException("Le titre doit contenir au moins 3 caractères");
        }

        if (priority == null) {
            throw new IllegalArgumentException("La priorité est obligatoire");
        }

        return repository.save(trimmedTitle, priority);
    }

    public Ticket getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException(id));
    }

    public List<Ticket> findAll() {
        return repository.findAll();
    }

    public Ticket updateStatus(Long id, TicketStatus newStatus) {
        Ticket ticket = getById(id);

        if (!isTransitionAllowed(ticket.status(), newStatus)) {
            throw new InvalidStatusTransitionException(ticket.status(), newStatus);
        }

        Ticket updated = new Ticket(ticket.id(), ticket.title(), ticket.priority(), newStatus);
        return repository.update(updated);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    boolean isTransitionAllowed(TicketStatus current, TicketStatus requested) {
        if (current == requested) {
            return false;
        }

        return switch (current) {
            case OPEN -> requested == TicketStatus.IN_PROGRESS || requested == TicketStatus.RESOLVED;
            case IN_PROGRESS -> requested == TicketStatus.RESOLVED;
            case RESOLVED -> false;
        };
    }
}
