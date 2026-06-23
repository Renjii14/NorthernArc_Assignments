package org.northernarc.flight.controller;

import jakarta.validation.Valid;
import org.northernarc.flight.dto.FlightRequestDTO;
import org.northernarc.flight.dto.FlightResponseDTO;
import org.northernarc.flight.dto.FlightUpdateDTO;
import org.northernarc.flight.model.Flight;
import org.northernarc.flight.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flights")
public class FlightController {
    @Autowired
    private FlightService flightService;

    @GetMapping
    public ResponseEntity<List<FlightResponseDTO>> getAllFlights() {
        return ResponseEntity.ok(flightService.getAllFlights());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlightResponseDTO> getFlightById(@PathVariable Long id){
        return ResponseEntity.ok(flightService.getFlightById(id));
    }

    @PostMapping
    public ResponseEntity<FlightResponseDTO> createFlight(@Valid @RequestBody FlightRequestDTO flightRequestDTO) {
        return ResponseEntity.status(201).body(flightService.addFlight(flightRequestDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FlightResponseDTO> updateFlight(@PathVariable Long id,@Valid @RequestBody FlightUpdateDTO flightUpdateDTO) {
        return ResponseEntity.ok(flightService.updateFlight(id, flightUpdateDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlight(@PathVariable Long id){
        flightService.deleteFlight(id);
        return ResponseEntity.noContent().build();
    }
}
