package org.northernarc.productrental.service;

import java.util.List;

import org.northernarc.productrental.dto.DashboardMetricsDTO;
import org.northernarc.productrental.dto.LoginRequestDTO;
import org.northernarc.productrental.dto.LoginResponseDTO;
import org.northernarc.productrental.model.RentalRecord;

public interface RentalService {
    List<RentalRecord> getAllRentals();
    RentalRecord getRentalById(Long rentalId);
    DashboardMetricsDTO getDashboard();


}
