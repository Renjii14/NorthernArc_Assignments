package org.northernarc.assessment4.serviceimpl;

import org.northernarc.assessment4.dto.CustomerSummaryDTO;
import org.northernarc.assessment4.dto.DashboardResponse;
import org.northernarc.assessment4.exception.AccountNotFoundException;
import org.northernarc.assessment4.exception.CustomerNotFoundException;
import org.northernarc.assessment4.model.Account;
import org.northernarc.assessment4.model.Customer;
import org.northernarc.assessment4.model.Transaction;
import org.northernarc.assessment4.model.UserRole;
import org.northernarc.assessment4.repository.AccountRepository;
import org.northernarc.assessment4.repository.CustomerRepository;
import org.northernarc.assessment4.repository.TransactionRepository;
import org.northernarc.assessment4.service.BankService;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BankServiceImpl implements BankService {

    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final PasswordEncoder passwordEncoder;

    // --- Core Entity Writing Persistence Methods ---
    @Override
    @Transactional
    public Customer saveCustomer(Customer customer) {
        // Task 8: Store encoded password for authentication
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        if (customer.getRole() == null) customer.setRole(UserRole.USER);
        return customerRepository.save(customer);
    }

    @Override
    @Transactional
    public Account saveAccount(Account account) {

        return accountRepository.save(account);
    }

    @Override
    @Transactional
    public void deleteAccount(String accountNumber) {
        if (!accountRepository.existsById(accountNumber)) {
            throw new AccountNotFoundException("Account not found: " + accountNumber);
        }
        accountRepository.deleteById(accountNumber);
    }

    @Override
    public List<Account> getAccountsByType(String accountType) {
        return accountRepository.findByAccountType(accountType);
    }

    @Override
    public List<Customer> getCustomersByBranch(String branch) {

        return customerRepository.findByBranch(branch);
    }

    @Override
    public List<Transaction> getTransactionsByType(String transactionType) {
        return transactionRepository.findByTransactionType(transactionType);
    }

    @Override
    public List<Account> getAccountsWithBalanceGreaterThan(double amount) {
        return accountRepository.findByBalanceGreaterThan(amount);
    }

    @Override
    public List<Customer> getRichCustomers(double threshold) {

        return customerRepository.findRichCustomers(threshold);
    }

    @Override
    public Map<String, Double> getTotalBalancePerBranch() {
        List<Object[]> rows = customerRepository.findTotalBalancePerBranch();
        Map<String, Double> result = new LinkedHashMap<>();
        for (Object[] row : rows) {
            result.put((String) row[0], (Double) row[1]);
        }
        return result;
    }

    @Override
    public List<Customer> getCustomersWithMultipleAccounts() {
        return customerRepository.findCustomersWithMultipleAccounts();
    }

    @Override
    public Transaction getLatestTransaction() {
        return transactionRepository.findLatestTransaction(PageRequest.of(0, 1)).stream().findFirst().orElse(null);
    }

    @Override
    public List<Account> getAccountsWithNoTransactions() {

        return accountRepository.findAccountsWithNoTransactions();
    }

    // Task 5: Increase balance through JPQL update query
    @Override
    @Transactional
    public void increaseAccountBalance(String accountNumber, double amount) {
        int updated = accountRepository.increaseBalance(accountNumber, amount);
        if (updated == 0) {
            throw new AccountNotFoundException("Account not found: " + accountNumber);
        }
    }

    // Task 6: Build pageable and sort by balance DESC in service layer
    @Override
    public Page<Account> getAllAccountsPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("balance").descending());
        return accountRepository.findAll(pageable);
    }

    @Override
    public CustomerSummaryDTO getCustomerSummary(Long customerId) {
        return customerRepository.findCustomerSummaryById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found: " + customerId));
    }

    @Override
    public DashboardResponse getDashboardMetrics() {
        // Final Challenge: Optimized aggregate queries only, no entity graph loading.
        long totalCustomers = customerRepository.count();

        Object[] accountAggregates = accountRepository.findAccountAggregates().stream().findFirst().orElse(new Object[]{0L, 0.0});
        long totalAccounts = ((Number) accountAggregates[0]).longValue();
        double totalBalance = ((Number) accountAggregates[1]).doubleValue();

        String topBranch = customerRepository.findTopBranchByTotalBalance(PageRequest.of(0, 1))
                .stream()
                .findFirst()
                .orElse("N/A");

        String highestBalanceCustomer = customerRepository.findHighestBalanceCustomer(PageRequest.of(0, 1))
                .stream()
                .findFirst()
                .orElse("N/A");

        return new DashboardResponse(
                totalCustomers,
                totalAccounts,
                totalBalance,
                topBranch,
                highestBalanceCustomer
        );
    }

}
