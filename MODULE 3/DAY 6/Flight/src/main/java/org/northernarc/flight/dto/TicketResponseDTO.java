package org.northernarc.flight.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketResponseDTO {
    private Long ticketId;

    private String seatNumber;
    private LocalDate bookingDate;
    private Double ticketPrice;
}
