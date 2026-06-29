package org.northernarc.productrental.dto;

import jakarta.persistence.Access;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public record DashboardMetricsDTO(
        long totalCustomers,
        long totalProducts,
        long activeRentals,
        long overdueRentals,
        double totalRentCollected,
        String topCategory,
        String highestPayingCustomer){
 
}
