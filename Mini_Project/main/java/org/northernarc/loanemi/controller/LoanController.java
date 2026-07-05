package org.northernarc.loanemi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;

import org.northernarc.loanemi.dto.CreateLoanRequest;
import org.northernarc.loanemi.dto.EmiPaymentRequestDTO;
import org.northernarc.loanemi.dto.LoanDashboardDTO;
import org.northernarc.loanemi.dto.LoanSummaryDTO;
import org.northernarc.loanemi.model.EmiSchedule;
import org.northernarc.loanemi.repository.LoanRepository;
import org.northernarc.loanemi.service.LoanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api")
@Tag(name = "Loans", description = "Loan and EMI APIs")
public class LoanController {
    private static final Logger log = LoggerFactory.getLogger(LoanController.class);

    private final LoanService loanService;
    private final LoanRepository loanRepository;

    public LoanController(LoanService loanService, LoanRepository loanRepository) {
        this.loanService = loanService;
        this.loanRepository = loanRepository;
    }

    @PostMapping("/loans")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    @Operation(summary = "Create loan and generate EMI schedule")
    public LoanSummaryDTO createLoan(@Valid @RequestBody CreateLoanRequest request) {
        log.info("Create loan requested for customerId={} loanType={} principal={} tenureMonths={}",
                request.getCustomerId(), request.getLoanType(), request.getPrincipalAmount(), request.getTenureMonths());
        return loanService.createLoan(request);
    }

    @GetMapping("/loans")
    @PreAuthorize("hasAnyRole('USER','MANAGER','ADMIN')")
    @Operation(summary = "Get loans with pagination and sorting")
    public Page<LoanSummaryDTO> getLoans(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("Get loans requested page={} size={}", page, size);
        return loanService.getLoans(page, size);
    }

    @GetMapping("/loans/{loanId}")
    @PreAuthorize("hasAnyRole('USER','MANAGER','ADMIN')")
    @Operation(summary = "Get loan by ID")
    public LoanSummaryDTO getLoan(@PathVariable Long loanId) {
        log.info("Get loan requested for loanId={}", loanId);
        return loanService.getLoan(loanId);
    }

    @GetMapping("/dashboard")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    @Operation(summary = "Get dashboard summary")
    public LoanDashboardDTO getDashboard() {
        log.info("Dashboard summary requested");
        return loanService.getDashboard();
    }

    @PatchMapping("/loans/interest-rate")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    @Operation(summary = "Bulk update interest rate by loan types")
    public int reviseInterestRates(
            @RequestParam List<String> loanTypes,
            @RequestParam Double annualInterestRate) {
        log.info("Bulk interest-rate revision requested loanTypes={} annualInterestRate={}", loanTypes, annualInterestRate);
        int totalUpdated = 0;
        for (String loanType : loanTypes) {
            totalUpdated += loanRepository.updateInterestRateByLoanType(loanType, annualInterestRate);
        }
        log.info("Bulk interest-rate revision completed totalUpdated={}", totalUpdated);
        return totalUpdated;
    }

    @PutMapping("/loans/{loanId}/interest")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    @Operation(summary = "Update a loan interest rate")
    public LoanSummaryDTO updateLoanInterest(@PathVariable Long loanId, @RequestParam Double rate) {
        log.info("Update interest rate requested for loanId={} rate={}", loanId, rate);
        return loanService.updateLoanInterest(loanId, rate);
    }

    @DeleteMapping("/loans/{loanId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete loan by ID")
    public void deleteLoan(@PathVariable Long loanId) {
        log.info("Delete loan requested for loanId={}", loanId);
        loanService.deleteLoan(loanId);
    }

    @PostMapping("/emis/pay")
    @PreAuthorize("hasAnyRole('USER','MANAGER','ADMIN')")
    @Operation(summary = "Pay EMI (supports partial payment)")
    public EmiSchedule payEmi(@Valid @RequestBody EmiPaymentRequestDTO request) {
        log.info("Pay EMI requested for emiId={} amount={} paymentMode={}",
                request.getEmiId(), request.getAmount(), request.getPaymentMode());
        return loanService.payEmi(request);
    }
}
