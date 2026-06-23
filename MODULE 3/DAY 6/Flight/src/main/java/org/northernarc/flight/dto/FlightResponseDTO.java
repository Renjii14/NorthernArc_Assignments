package org.northernarc.flight.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightResponseDTO {

    private Long flight_id;

    private String flightNumber;
    private String airlineName;
    private String source;
    private String destination;
    private LocalDate departureDate;
    private LocalTime departureTime;
    private LocalTime arrivalTime;
    private Double fare;
    private Integer totalSeats;
    private Integer availableSeats;
    private String status;
}
