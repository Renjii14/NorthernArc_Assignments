package org.northernarc.flight.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "flight_id")
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

    @OneToMany(mappedBy = "flight",cascade = CascadeType.ALL)
    private List<Ticket> ticketList;


}
