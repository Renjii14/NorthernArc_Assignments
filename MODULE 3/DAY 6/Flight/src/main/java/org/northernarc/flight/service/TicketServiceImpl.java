package org.northernarc.flight.service;

import org.northernarc.flight.dto.TicketRequestDTO;
import org.northernarc.flight.dto.TicketResponseDTO;
import org.northernarc.flight.dto.TicketUpdateDTO;
import org.northernarc.flight.exception.TicketNotFound;
import org.northernarc.flight.model.Ticket;
import org.northernarc.flight.repository.TicketRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    private TicketRepo ticketRepo;

    @Override
    public TicketResponseDTO bookTicket(TicketRequestDTO ticketRequestDTO) {
        Ticket ticket=mapToEntity(ticketRequestDTO);
        Ticket savedTicket=ticketRepo.save(ticket);
        return mapToResponseDTO(savedTicket);
    }

    @Override
    public TicketResponseDTO getTicketById(Long ticketId) {
        Ticket ticket=ticketRepo.findById(ticketId).orElseThrow(()->new TicketNotFound("Ticket Not Found"));
        return mapToResponseDTO(ticket);
    }

    @Override
    public List<TicketResponseDTO> getAllTickets() {
        return ticketRepo.findAll().stream().map(this::mapToResponseDTO).toList();
    }

    @Override
    public TicketResponseDTO updateTicket(Long ticketId, TicketUpdateDTO ticketUpdateDTO) {
        Ticket ticket=ticketRepo.findById(ticketId).orElseThrow(()->new TicketNotFound("Ticket Not Found"));
        ticket.setSeatNumber(ticketUpdateDTO.getSeatNumber());
        ticket.setBookingDate(ticketUpdateDTO.getBookingDate());
        ticket.setTicketPrice(ticketUpdateDTO.getTicketPrice());
        Ticket updatedTicket=ticketRepo.save(ticket);
        return mapToResponseDTO(updatedTicket);
    }

    @Override
    public void cancelTicket(Long ticketId) {
          Ticket ticket=ticketRepo.findById(ticketId).orElseThrow(()->new TicketNotFound("Ticket Not Found"));
          ticketRepo.delete(ticket);
    }

    private Ticket mapToEntity(TicketRequestDTO dto) {
        Ticket ticket = new Ticket();
        ticket.setSeatNumber(dto.getSeatNumber());
        ticket.setBookingDate(dto.getBookingDate());
        ticket.setTicketPrice(dto.getTicketPrice());
        return ticket;
    }

    private TicketResponseDTO mapToResponseDTO(Ticket ticket){
        TicketResponseDTO dto=new TicketResponseDTO();
        dto.setSeatNumber(ticket.getSeatNumber());
        dto.setBookingDate(ticket.getBookingDate());
        dto.setTicketPrice(ticket.getTicketPrice());
        return dto;
    }
}
