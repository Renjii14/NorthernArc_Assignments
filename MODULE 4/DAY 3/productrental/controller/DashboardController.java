package org.northernarc.productrental.controller;

import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.northernarc.productrental.dto.DashboardMetricsDTO;
import org.northernarc.productrental.serviceimpl.RentalServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Dashboard", description = "Dashboard metrics APIs")
@SecurityRequirement(name = "bearerAuth")
public class DashboardController {

    @Autowired
    private RentalServiceImpl rentalService;

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get rental dashboard metrics")
    public DashboardMetricsDTO getDashboard() {
        return rentalService.getDashboard();
    }
}
