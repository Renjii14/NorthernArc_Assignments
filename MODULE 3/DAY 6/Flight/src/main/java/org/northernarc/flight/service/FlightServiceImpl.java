package org.northernarc.flight.service;

import org.northernarc.flight.dto.FlightRequestDTO;
import org.northernarc.flight.dto.FlightResponseDTO;
import org.northernarc.flight.dto.FlightUpdateDTO;
import org.northernarc.flight.exception.FlightNotFound;
import org.northernarc.flight.model.Flight;
import org.northernarc.flight.repository.FlightRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class FlightServiceImpl implements FlightService {

    @Autowired
    private FlightRepo flightRepo;

    @Override
    public FlightResponseDTO addFlight(FlightRequestDTO flightRequestDTO) {
        Flight flight=mapToEntity(flightRequestDTO);
        Flight savedFlight=flightRepo.save(flight);
        return mapToResponseDTO(savedFlight);
    }

    @Override
    public FlightResponseDTO getFlightById(Long flightId) {
        Flight flight=flightRepo.findById(flightId).orElseThrow(()->new FlightNotFound("Flight Not Found"));
        return mapToResponseDTO(flight);
    }

    @Override
    public List<FlightResponseDTO> getAllFlights() {
        return flightRepo.findAll().stream().map(this::mapToResponseDTO).toList();
    }

    @Override
    public FlightResponseDTO updateFlight(Long flightId, FlightUpdateDTO flightUpdateDTO) {
        Flight flight=flightRepo.findById(flightId).orElseThrow(()->new FlightNotFound("Flight Not Found"));
        flight.setAirlineName(flightUpdateDTO.getAirlineName());
        flight.setDepartureDate(flightUpdateDTO.getDepartureDate());
        flight.setDepartureTime(flightUpdateDTO.getDepartureTime());
        flight.setArrivalTime(flightUpdateDTO.getArrivalTime());
        flight.setFare(flightUpdateDTO.getFare());
        flight.setTotalSeats(flightUpdateDTO.getTotalSeats());
        flight.setStatus(flightUpdateDTO.getStatus());
        Flight updatedFlight=flightRepo.save(flight);
        return mapToResponseDTO(updatedFlight);
    }

    @Override
    public void deleteFlight(Long flightId) {
   Flight flight=flightRepo.findById(flightId).orElseThrow(()->new FlightNotFound("Flight Not Found"));
   flightRepo.delete(flight);
    }

    private Flight mapToEntity(FlightRequestDTO flightRequestDTO){
        Flight flight=new Flight();
       flight.setFlightNumber(flightRequestDTO.getFlightNumber());
       flight.setAirlineName(flightRequestDTO.getAirlineName());
        flight.setSource(flightRequestDTO.getSource());
        flight.setDestination(flightRequestDTO.getDestination());
        flight.setDepartureDate(flightRequestDTO.getDepartureDate());
        flight.setDepartureTime(flightRequestDTO.getDepartureTime());
        flight.setArrivalTime(flightRequestDTO.getArrivalTime());
        flight.setFare(flightRequestDTO.getFare());
        flight.setAvailableSeats(flightRequestDTO.getAvailableSeats());
        flight.setTotalSeats(flightRequestDTO.getTotalSeats());
        return flight;

    }

    private FlightResponseDTO mapToResponseDTO(Flight flight){
        FlightResponseDTO dto=new FlightResponseDTO();
        dto.setFlight_id(flight.getFlight_id());
        dto.setFlightNumber(flight.getFlightNumber());
        dto.setAirlineName(flight.getAirlineName());
        dto.setSource(flight.getSource());
        dto.setDestination(flight.getDestination());
        dto.setDepartureDate(flight.getDepartureDate());
        dto.setDepartureTime(flight.getDepartureTime());
        dto.setArrivalTime(flight.getArrivalTime());
        dto.setFare(flight.getFare());
        dto.setAvailableSeats(flight.getAvailableSeats());
        dto.setTotalSeats(flight.getTotalSeats());
        return dto;
    }
}
