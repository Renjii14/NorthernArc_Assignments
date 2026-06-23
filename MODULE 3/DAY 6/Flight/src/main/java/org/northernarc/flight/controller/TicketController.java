package org.northernarc.flight.controller;
import jakarta.validation.Valid;
import org.northernarc.flight.dto.TicketRequestDTO;
import org.northernarc.flight.dto.TicketResponseDTO;
import org.northernarc.flight.dto.TicketUpdateDTO;
import org.northernarc.flight.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @GetMapping
    public ResponseEntity<List<TicketResponseDTO>> getAllTickets() {
        return ResponseEntity.ok(ticketService.getAllTickets());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketResponseDTO> getTicketById(
            @PathVariable Long id) {

        return ResponseEntity.ok(ticketService.getTicketById(id));
    }

    @PostMapping
    public ResponseEntity<TicketResponseDTO> bookTicket(
            @Valid @RequestBody TicketRequestDTO ticketRequestDTO) {

        return ResponseEntity.status(201)
                .body(ticketService.bookTicket(ticketRequestDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TicketResponseDTO> updateTicket(
            @PathVariable Long id,
            @Valid @RequestBody TicketUpdateDTO ticketUpdateDTO) {

        return ResponseEntity.ok(
                ticketService.updateTicket(id, ticketUpdateDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelTicket(@PathVariable Long id) {

        ticketService.cancelTicket(id);

        return ResponseEntity.noContent().build();
    }
}
