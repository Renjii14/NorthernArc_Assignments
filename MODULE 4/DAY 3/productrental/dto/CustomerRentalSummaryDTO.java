package org.northernarc.productrental.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRentalSummaryDTO{
        String customerName;
        String city;
        Long numberOfRentals;
        Double totalRentPaid;
}
