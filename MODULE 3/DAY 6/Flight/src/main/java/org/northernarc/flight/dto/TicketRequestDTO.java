package org.northernarc.flight.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketRequestDTO {

@NotNull
    private String seatNumber;
@NotNull
    private LocalDate bookingDate;
@Positive
    private Double ticketPrice;
}
