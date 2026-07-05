package org.northernarc.loanemi.service;

import java.time.LocalDate;

import org.northernarc.loanemi.dto.CreateLoanRequest;
import org.northernarc.loanemi.dto.EmiPaymentRequestDTO;
import org.northernarc.loanemi.dto.LoanDashboardDTO;
import org.northernarc.loanemi.dto.LoanSummaryDTO;
import org.northernarc.loanemi.model.EmiSchedule;
import org.springframework.data.domain.Page;

public interface LoanService {

    LoanSummaryDTO createLoan(CreateLoanRequest request);

    Page<LoanSummaryDTO> getLoans(int page, int size);

    LoanSummaryDTO getLoan(Long loanId);

    LoanDashboardDTO getDashboard();

    void recalculateOverduePenalties(LocalDate currentDate);

    EmiSchedule payEmi(EmiPaymentRequestDTO request);

    LoanSummaryDTO updateLoanInterest(Long loanId, Double rate);

    void deleteLoan(Long loanId);
}
