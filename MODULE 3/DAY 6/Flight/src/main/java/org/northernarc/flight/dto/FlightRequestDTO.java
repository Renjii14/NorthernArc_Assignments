package org.northernarc.flight.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightRequestDTO {

    @NotBlank
    private String flightNumber;
    @NotBlank
    private String airlineName;
    @NotBlank
    private String source;
    @NotBlank
    private String destination;
    @NotNull
    private LocalDate departureDate;
    @NotNull
    private LocalTime departureTime;
    @NotNull
    private LocalTime arrivalTime;
    @Positive
    private Double fare;
    @Positive
    private Integer totalSeats;
    @Positive
    private Integer availableSeats;
}
