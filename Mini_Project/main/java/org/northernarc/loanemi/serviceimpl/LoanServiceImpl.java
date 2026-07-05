package org.northernarc.loanemi.serviceimpl;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.northernarc.loanemi.dto.CreateLoanRequest;
import org.northernarc.loanemi.dto.EmiPaymentRequestDTO;
import org.northernarc.loanemi.dto.LoanDashboardDTO;
import org.northernarc.loanemi.dto.LoanSummaryDTO;
import org.northernarc.loanemi.enums.EmiStatus;
import org.northernarc.loanemi.enums.LoanStatus;
import org.northernarc.loanemi.exception.CustomerNotFoundException;
import org.northernarc.loanemi.exception.LoanNotFoundException;
import org.northernarc.loanemi.model.Customer;
import org.northernarc.loanemi.model.EmiPayment;
import org.northernarc.loanemi.model.EmiSchedule;
import org.northernarc.loanemi.model.Loan;
import org.northernarc.loanemi.repository.CustomerRepository;
import org.northernarc.loanemi.repository.EmiPaymentRepository;
import org.northernarc.loanemi.repository.EmiScheduleRepository;
import org.northernarc.loanemi.repository.LoanRepository;
import org.northernarc.loanemi.service.LoanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoanServiceImpl implements LoanService {
    private static final Logger log = LoggerFactory.getLogger(LoanServiceImpl.class);

    private final LoanRepository loanRepository;
    private final CustomerRepository customerRepository;
    private final EmiScheduleRepository emiScheduleRepository;
    private final EmiPaymentRepository emiPaymentRepository;

    public LoanServiceImpl(LoanRepository loanRepository, CustomerRepository customerRepository,
                           EmiScheduleRepository emiScheduleRepository,
                           EmiPaymentRepository emiPaymentRepository) {
        this.loanRepository = loanRepository;
        this.customerRepository = customerRepository;
        this.emiScheduleRepository = emiScheduleRepository;
        this.emiPaymentRepository = emiPaymentRepository;
    }

    @Override
    @Transactional
    public LoanSummaryDTO createLoan(CreateLoanRequest request) {
        log.info("Creating loan for customerId={} loanType={} principal={} tenureMonths={}",
                request.getCustomerId(), request.getLoanType(), request.getPrincipalAmount(), request.getTenureMonths());
        if (request.getPrincipalAmount() <= 0) {
            log.warn("Loan creation rejected: invalid principalAmount={}", request.getPrincipalAmount());
            throw new IllegalArgumentException("Loan cannot be approved if principalAmount <= 0");
        }
        if (request.getTenureMonths() <= 0) {
            log.warn("Loan creation rejected: invalid tenureMonths={}", request.getTenureMonths());
            throw new IllegalArgumentException("Tenure must be greater than zero");
        }

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found: " + request.getCustomerId()));

        Loan loan = new Loan();
        loan.setLoanType(request.getLoanType());
        loan.setPrincipalAmount(request.getPrincipalAmount());
        loan.setAnnualInterestRate(request.getAnnualInterestRate());
        loan.setTenureMonths(request.getTenureMonths());
        loan.setLoanStatus("ACTIVE");
        loan.setDisbursementDate(LocalDate.now());
        loan.setCustomer(customer);

        double emi = calculateEmi(loan.getPrincipalAmount(), loan.getAnnualInterestRate(), loan.getTenureMonths());
        loan.setEmiAmount(emi);
        generateEmiSchedule(loan);

        Loan saved = loanRepository.save(loan);
        log.info("Loan created successfully loanId={} customerId={} emiAmount={}",
                saved.getLoanId(), saved.getCustomer().getCustomerId(), saved.getEmiAmount());
        return toSummary(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LoanSummaryDTO> getLoans(int page, int size) {
        log.info("Fetching loans page={} size={}", page, size);
        PageRequest pageable = PageRequest.of(page, size, Sort.by("principalAmount").descending());
        return loanRepository.findAll(pageable).map(this::toSummary);
    }

    @Override
    @Transactional(readOnly = true)
    public LoanSummaryDTO getLoan(Long loanId) {
        log.info("Fetching loan details loanId={}", loanId);
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException("Loan not found: " + loanId));
        return toSummary(loan);
    }

    @Override
    @Transactional(readOnly = true)
    public LoanDashboardDTO getDashboard() {
        log.info("Building dashboard metrics");
        long totalCustomers = customerRepository.count();
        long totalLoans = loanRepository.count();
        long activeLoans = loanRepository.findActiveLoans().size();
        long closedLoans = loanRepository.findByLoanStatus(LoanStatus.CLOSED).size();
        long overdueCount = defaultZeroLong(emiScheduleRepository.countOverdueEmis());

        double totalEmiCollected = defaultZero(loanRepository.findTotalEmiCollected());
        double totalPenaltyCollected = defaultZero(loanRepository.findTotalPenaltyCollected());
        double averageInterestRate = defaultZero(loanRepository.findAverageInterestRate());

        String highestOutstandingLoan = extractHighestOutstandingLoan();
        String highestPayingCustomer = extractHighestPayingCustomer();
        long npaAccounts = defaultZeroLong(emiScheduleRepository.countNpaAccounts());

        return new LoanDashboardDTO(totalCustomers, totalLoans, activeLoans, closedLoans, overdueCount,
                totalEmiCollected, totalPenaltyCollected, averageInterestRate, highestOutstandingLoan,
                highestPayingCustomer, npaAccounts);
    }

    @Override
    @Transactional
    public void recalculateOverduePenalties(LocalDate currentDate) {
        List<EmiSchedule> schedules = emiScheduleRepository.findUnpaidPastDueSchedules(currentDate);
        log.info("Recalculating overdue penalties for schedulesCount={} currentDate={}", schedules.size(), currentDate);
        for (EmiSchedule schedule : schedules) {
            long dpd = ChronoUnit.DAYS.between(schedule.getDueDate(), currentDate);
            schedule.setDaysPastDue((int) dpd);
            schedule.setStatus("OVERDUE");
            double penalty = (schedule.getAmountDue() * 0.02) + (schedule.getDaysPastDue() * 50);
            schedule.setPenaltyAmount(penalty);
        }
    }

    @Override
    @Transactional
    public EmiSchedule payEmi(EmiPaymentRequestDTO request) {
        log.info("Processing EMI payment emiId={} amount={} paymentMode={}",
                request.getEmiId(), request.getAmount(), request.getPaymentMode());
        EmiSchedule schedule = emiScheduleRepository.findByIdForUpdate(request.getEmiId());
        if (schedule == null) {
            log.warn("EMI payment rejected: schedule not found emiId={}", request.getEmiId());
            throw new LoanNotFoundException("EMI schedule not found: " + request.getEmiId());
        }
        if (LoanStatus.CLOSED.equals(schedule.getLoan().getLoanStatusEnum())) {
            log.warn("EMI payment rejected: loan already closed loanId={}", schedule.getLoan().getLoanId());
            throw new IllegalArgumentException("Closed loans cannot accept further EMI payments");
        }

        double outstanding = payableForSchedule(schedule);
        if (request.getAmount() > outstanding) {
            log.warn("EMI payment rejected: amount exceeds outstanding emiId={} requested={} outstanding={}",
                    request.getEmiId(), request.getAmount(), outstanding);
            throw new IllegalArgumentException("Payment exceeds payable amount");
        }

        EmiPayment payment = new EmiPayment();
        payment.setAmount(request.getAmount());
        payment.setPaymentMode(request.getPaymentMode());
        payment.setPaymentDate(LocalDate.now());
        payment.setReferenceNumber(request.getReferenceNumber());
        payment.setEmiSchedule(schedule);
        try {
            emiPaymentRepository.save(payment);
        } catch (DataIntegrityViolationException ex) {
            log.warn("EMI payment rejected: duplicate referenceNumber={}", request.getReferenceNumber());
            throw new IllegalArgumentException("Duplicate payment reference number");
        }

        double newPaid = defaultZero(schedule.getAmountPaid()) + request.getAmount();
        schedule.setAmountPaid(newPaid);
        schedule.setPaymentDate(LocalDate.now());

        if (newPaid >= schedule.getAmountDue() + schedule.getPenaltyAmount()) {
            schedule.setStatus("PAID");
            schedule.setPenaltyAmount(0.0);
            schedule.setDaysPastDue(0);
            closeLoanIfCompleted(schedule.getLoan().getLoanId());
        } else {
            schedule.setStatus("PENDING");
        }
        log.info("EMI payment processed emiId={} updatedStatus={} amountPaid={}",
                request.getEmiId(), schedule.getStatus(), schedule.getAmountPaid());
        return schedule;
    }

    @Override
    @Transactional
    public LoanSummaryDTO updateLoanInterest(Long loanId, Double rate) {
        log.info("Updating loan interest rate loanId={} rate={}", loanId, rate);
        if (rate == null || rate <= 0) {
            log.warn("Loan interest update rejected: invalid rate={} loanId={}", rate, loanId);
            throw new IllegalArgumentException("Interest rate must be greater than zero");
        }
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException("Loan not found: " + loanId));
        loan.setAnnualInterestRate(rate);
        return toSummary(loan);
    }

    @Override
    @Transactional
    public void deleteLoan(Long loanId) {
        log.info("Deleting loan loanId={}", loanId);
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException("Loan not found: " + loanId));
        loanRepository.delete(loan);
        log.info("Loan deleted successfully loanId={}", loanId);
    }

    private double calculateEmi(double principal, double annualInterestRate, int tenureMonths) {
        double monthlyRate = annualInterestRate / (12 * 100);
        if (monthlyRate == 0) {
            return principal / tenureMonths;
        }
        double factor = Math.pow(1 + monthlyRate, tenureMonths);
        return (principal * monthlyRate * factor) / (factor - 1);
    }

    private void generateEmiSchedule(Loan loan) {
        double outstanding = loan.getPrincipalAmount();
        double monthlyRate = loan.getAnnualInterestRate() / (12 * 100);
        LocalDate startDate = loan.getDisbursementDate();

        for (int installment = 1; installment <= loan.getTenureMonths(); installment++) {
            double interestComponent = outstanding * monthlyRate;
            double principalComponent = loan.getEmiAmount() - interestComponent;
            if (installment == loan.getTenureMonths()) {
                principalComponent = outstanding;
            }
            double amountDue = principalComponent + interestComponent;
            outstanding = Math.max(0, outstanding - principalComponent);

            EmiSchedule schedule = new EmiSchedule();
            schedule.setInstallmentNumber(installment);
            schedule.setDueDate(startDate.plusMonths(installment));
            schedule.setAmountDue(amountDue);
            schedule.setPrincipalComponent(principalComponent);
            schedule.setInterestComponent(interestComponent);
            schedule.setAmountPaid(0.0);
            schedule.setStatus("PENDING");
            schedule.setDaysPastDue(0);
            schedule.setPenaltyAmount(0.0);
            loan.addEmiSchedule(schedule);
        }
    }

    private LoanSummaryDTO toSummary(Loan loan) {
        return new LoanSummaryDTO(
                loan.getLoanId(),
                loan.getLoanType(),
                loan.getPrincipalAmount(),
                loan.getAnnualInterestRate(),
                loan.getTenureMonths(),
                loan.getEmiAmount(),
                loan.getLoanStatus(),
                loan.getCustomer().getCustomerId(),
                loan.getCustomer().getCustomerName(),
                loan.getCustomer().getCity()
        );
    }

    private double defaultZero(Double value) {
        return value == null ? 0.0 : value;
    }

    private long defaultZeroLong(Long value) {
        return value == null ? 0L : value;
    }

    private void closeLoanIfCompleted(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException("Loan not found: " + loanId));
        boolean hasPending = loan.getEmiSchedules().stream().anyMatch(e -> !EmiStatus.PAID.equals(e.getStatusEnum()));
        if (!hasPending) {
            loan.setLoanStatus("CLOSED");
            log.info("Loan marked as CLOSED loanId={}", loanId);
        }
    }

    private double payableForSchedule(EmiSchedule schedule) {
        if (EmiStatus.OVERDUE.equals(schedule.getStatusEnum())) {
            recalculatePenalty(schedule, LocalDate.now());
        }
        return (schedule.getAmountDue() + schedule.getPenaltyAmount()) - defaultZero(schedule.getAmountPaid());
    }

    private void recalculatePenalty(EmiSchedule schedule, LocalDate currentDate) {
        if (schedule.getDueDate().isBefore(currentDate)) {
            long dpd = ChronoUnit.DAYS.between(schedule.getDueDate(), currentDate);
            schedule.setDaysPastDue((int) dpd);
            schedule.setPenaltyAmount((schedule.getAmountDue() * 0.02) + (schedule.getDaysPastDue() * 50));
            schedule.setStatus("OVERDUE");
        }
    }

    private String extractHighestOutstandingLoan() {
        List<Object[]> ranking = loanRepository.findLoanOutstandingRanking();
        if (ranking.isEmpty()) {
            return "N/A";
        }
        Object[] row = ranking.get(0);
        return "Loan#" + row[0] + " (" + row[1] + ")";
    }

    private String extractHighestPayingCustomer() {
        List<Object[]> ranking = customerRepository.findHighestPayingCustomers();
        if (ranking.isEmpty()) {
            return "N/A";
        }
        Object[] row = ranking.get(0);
        return "Customer#" + row[0] + " (" + row[1] + ")";
    }
}
