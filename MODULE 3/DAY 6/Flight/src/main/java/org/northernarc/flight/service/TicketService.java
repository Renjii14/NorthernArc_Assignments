package org.northernarc.flight.service;

import org.northernarc.flight.dto.TicketRequestDTO;
import org.northernarc.flight.dto.TicketResponseDTO;
import org.northernarc.flight.dto.TicketUpdateDTO;

import java.util.List;

public interface TicketService {

    TicketResponseDTO bookTicket(TicketRequestDTO ticketRequestDTO);

    TicketResponseDTO getTicketById(Long ticketId);

    List<TicketResponseDTO> getAllTickets();

    TicketResponseDTO updateTicket(Long ticketId, TicketUpdateDTO ticketUpdateDTO);

    void cancelTicket(Long ticketId);
}
