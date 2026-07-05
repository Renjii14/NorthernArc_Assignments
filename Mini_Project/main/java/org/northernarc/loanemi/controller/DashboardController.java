package org.northernarc.loanemi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.northernarc.loanemi.dto.LoanDashboardDTO;
import org.northernarc.loanemi.service.LoanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Dashboard", description = "Analytics and dashboard APIs")
public class DashboardController {
    private static final Logger log = LoggerFactory.getLogger(DashboardController.class);

    private final LoanService loanService;

    public DashboardController(LoanService loanService) {
        this.loanService = loanService;
    }

    @GetMapping("/dashboard")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    @Operation(summary = "Get top-level dashboard metrics")
    public LoanDashboardDTO getDashboard() {
        log.info("Dashboard metrics requested");
        return loanService.getDashboard();
    }

    @GetMapping("/api/dashboard/admin")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Admin dashboard endpoint")
    public LoanDashboardDTO getAdminDashboard() {
        log.info("Admin dashboard metrics requested");
        return loanService.getDashboard();
    }
}
