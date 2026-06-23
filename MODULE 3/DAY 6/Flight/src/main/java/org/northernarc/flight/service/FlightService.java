package org.northernarc.flight.service;

import org.northernarc.flight.dto.FlightRequestDTO;
import org.northernarc.flight.dto.FlightResponseDTO;
import org.northernarc.flight.dto.FlightUpdateDTO;

import java.util.List;

public interface FlightService {

    FlightResponseDTO addFlight(FlightRequestDTO flightRequestDTO);

    FlightResponseDTO getFlightById(Long flightId);

    List<FlightResponseDTO> getAllFlights();

    FlightResponseDTO updateFlight(Long flightId, FlightUpdateDTO flightUpdateDTO);

    void deleteFlight(Long flightId);
}
