package org.northernarc.week5_assess.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;

import jakarta.persistence.PersistenceException;
import org.northernarc.week5_assess.entity.Account;
import org.northernarc.week5_assess.entity.Customer;

@DataJpaTest
@DisplayName("Account repository behaviour")
class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private Customer baseCustomer;
    private Account baseAccount;

    @BeforeEach
    void beforeEach(TestInfo testInfo) {
        baseCustomer = new Customer();
        baseCustomer.setName("John Doe");
        baseCustomer.setEmail("john@example.com");
        baseCustomer.setPhone("9876543210");

        baseAccount = new Account();
        baseAccount.setAccountNumber("SB100001");
        baseAccount.setAccountType("SAVINGS");
        baseAccount.setBalance(new BigDecimal("5000.00"));

        System.out.println("Running test: " + testInfo.getDisplayName());
    }

    /*
     * Test Case: shouldSaveAccountSuccessfully
     * Description: Validates shouldSaveAccountSuccessfully behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
    @Test
    @DisplayName("Save account successfully")
    void shouldSaveAccountSuccessfully() {
        Customer savedCustomer = persistCustomer("John Doe", "john@example.com", "9876543210");
        long countBeforeSave = accountRepository.count();
        Account account = newAccount("SB100001", "SAVINGS", new BigDecimal("5000.00"), savedCustomer);

        Account savedAccount = accountRepository.saveAndFlush(account);

        assertThat(savedAccount.getId()).isNotNull();
        assertThat(savedAccount.getAccountNumber()).isEqualTo("SB100001");
        assertThat(savedAccount.getAccountType()).isEqualTo("SAVINGS");
        assertThat(savedAccount.getBalance()).isEqualByComparingTo("5000.00");
        assertThat(accountRepository.count()).isEqualTo(countBeforeSave + 1);
    }

    /*
     * Test Case: shouldReturnAccountWhenIdExists
     * Description: Validates shouldReturnAccountWhenIdExists behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
    @Test
    void shouldReturnAccountWhenIdExists() {
        Customer savedCustomer = persistCustomer("John Doe", "john@example.com", "9876543210");
        Account savedAccount = accountRepository.saveAndFlush(
                newAccount("SB100001", "SAVINGS", new BigDecimal("5000.00"), savedCustomer));

        Optional<Account> result = accountRepository.findById(savedAccount.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(savedAccount.getId());
        assertThat(result.get().getAccountNumber()).isEqualTo("SB100001");
    }

    /*
     * Test Case: shouldReturnEmptyWhenAccountIdDoesNotExist
     * Description: Validates shouldReturnEmptyWhenAccountIdDoesNotExist behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
    @Test
    void shouldReturnEmptyWhenAccountIdDoesNotExist() {
        Optional<Account> result = accountRepository.findById(Long.MAX_VALUE);

        assertThat(result).isEmpty();
    }

    /*
     * Test Case: shouldReturnAccountWhenAccountNumberExists
     * Description: Validates shouldReturnAccountWhenAccountNumberExists behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
    @Test
    void shouldReturnAccountWhenAccountNumberExists() {
        Customer savedCustomer = persistCustomer("John Doe", "john@example.com", "9876543210");
        accountRepository.saveAndFlush(newAccount("SB100001", "SAVINGS", new BigDecimal("5000.00"), savedCustomer));

        Optional<Account> result = accountRepository.findByAccountNumber("SB100001");

        assertThat(result).isPresent();
        assertThat(result.get().getAccountNumber()).isEqualTo("SB100001");
    }

    /*
     * Test Case: shouldReturnEmptyWhenAccountNumberDoesNotExist
     * Description: Validates shouldReturnEmptyWhenAccountNumberDoesNotExist behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
    @Test
    void shouldReturnEmptyWhenAccountNumberDoesNotExist() {
        Optional<Account> result = accountRepository.findByAccountNumber("UNKNOWN123");

        assertThat(result).isEmpty();
    }

    /*
     * Test Case: shouldReturnTrueWhenAccountNumberExists
     * Description: Validates shouldReturnTrueWhenAccountNumberExists behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
    @Test
    void shouldReturnTrueWhenAccountNumberExists() {
        Customer savedCustomer = persistCustomer("John Doe", "john@example.com", "9876543210");
        accountRepository.saveAndFlush(newAccount("SB100001", "SAVINGS", new BigDecimal("5000.00"), savedCustomer));

        boolean exists = accountRepository.existsByAccountNumber("SB100001");

        assertThat(exists).isTrue();
    }

    /*
     * Test Case: shouldReturnFalseWhenAccountNumberDoesNotExist
     * Description: Validates shouldReturnFalseWhenAccountNumberDoesNotExist behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
    @Test
    void shouldReturnFalseWhenAccountNumberDoesNotExist() {
        boolean exists = accountRepository.existsByAccountNumber("UNKNOWN123");

        assertThat(exists).isFalse();
    }

    /*
     * Test Case: shouldReturnSingleAccountWhenCustomerHasOneAccount
     * Description: Validates shouldReturnSingleAccountWhenCustomerHasOneAccount behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
    @Test
    void shouldReturnSingleAccountWhenCustomerHasOneAccount() {
        Customer savedCustomer = persistCustomer("John Doe", "john@example.com", "9876543210");
        accountRepository.saveAndFlush(newAccount("SB100001", "SAVINGS", new BigDecimal("5000.00"), savedCustomer));

        List<Account> accounts = accountRepository.findByCustomerId(savedCustomer.getId());

        assertThat(accounts).hasSize(1);
        assertThat(accounts.get(0).getAccountNumber()).isEqualTo("SB100001");
    }

    /*
     * Test Case: shouldReturnMultipleAccountsWhenCustomerHasMultipleAccounts
     * Description: Validates shouldReturnMultipleAccountsWhenCustomerHasMultipleAccounts behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
    @Test
    void shouldReturnMultipleAccountsWhenCustomerHasMultipleAccounts() {
        Customer savedCustomer = persistCustomer("John Doe", "john@example.com", "9876543210");
        accountRepository.saveAndFlush(newAccount("SB100001", "SAVINGS", new BigDecimal("5000.00"), savedCustomer));
        accountRepository.saveAndFlush(newAccount("CA100001", "CURRENT", new BigDecimal("9000.00"), savedCustomer));

        List<Account> accounts = accountRepository.findByCustomerId(savedCustomer.getId());

        assertThat(accounts).hasSize(2);
        assertThat(accounts)
                .extracting(Account::getAccountNumber)
                .containsExactlyInAnyOrder("SB100001", "CA100001");
    }

    /*
     * Test Case: shouldReturnEmptyListWhenCustomerHasNoAccounts
     * Description: Validates shouldReturnEmptyListWhenCustomerHasNoAccounts behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
    @Test
    void shouldReturnEmptyListWhenCustomerHasNoAccounts() {
        Customer savedCustomer = persistCustomer("Jane Roe", "jane@example.com", "9123456789");

        List<Account> accounts = accountRepository.findByCustomerId(savedCustomer.getId());

        assertThat(accounts).isEmpty();
    }

    /*
     * Test Case: shouldUpdateAccountSuccessfully
     * Description: Validates shouldUpdateAccountSuccessfully behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
    @Test
    void shouldUpdateAccountSuccessfully() {
        Customer savedCustomer = persistCustomer("John Doe", "john@example.com", "9876543210");
        Account savedAccount = accountRepository.saveAndFlush(
                newAccount("SB100001", "SAVINGS", new BigDecimal("5000.00"), savedCustomer));

        savedAccount.setBalance(new BigDecimal("7500.00"));
        savedAccount.setAccountType("CURRENT");
        Account updated = accountRepository.saveAndFlush(savedAccount);

        Optional<Account> result = accountRepository.findById(updated.getId());
        assertThat(result).isPresent();
        assertThat(result.get().getBalance()).isEqualByComparingTo("7500.00");
        assertThat(result.get().getAccountType()).isEqualTo("CURRENT");
    }

    /*
     * Test Case: shouldDeleteAccountSuccessfully
     * Description: Validates shouldDeleteAccountSuccessfully behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
    @Test
    void shouldDeleteAccountSuccessfully() {
        Customer savedCustomer = persistCustomer("John Doe", "john@example.com", "9876543210");
        Account savedAccount = accountRepository.saveAndFlush(
                newAccount("SB100001", "SAVINGS", new BigDecimal("5000.00"), savedCustomer));
        long countBeforeDelete = accountRepository.count();

        accountRepository.delete(savedAccount);
        accountRepository.flush();

        assertThat(accountRepository.count()).isEqualTo(countBeforeDelete - 1);
        assertThat(accountRepository.findById(savedAccount.getId())).isEmpty();
    }

    /*
     * Test Case: shouldFailWhenSavingDuplicateAccountNumber
     * Description: Validates shouldFailWhenSavingDuplicateAccountNumber behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
    @Test
    void shouldFailWhenSavingDuplicateAccountNumber() {
        Customer firstCustomer = persistCustomer("John Doe", "john@example.com", "9876543210");
        Customer secondCustomer = persistCustomer("Jane Roe", "jane@example.com", "9123456789");
        accountRepository.saveAndFlush(newAccount("SB100001", "SAVINGS", new BigDecimal("5000.00"), firstCustomer));

        Account duplicateAccount = newAccount("SB100001", "CURRENT", new BigDecimal("3000.00"), secondCustomer);

        assertThatThrownBy(() -> accountRepository.saveAndFlush(duplicateAccount))
                .isInstanceOfAny(DataIntegrityViolationException.class, PersistenceException.class);
    }

    /*
     * Test Case: shouldReturnEmptyWhenFindByAccountNumberIsNull
     * Description: Validates shouldReturnEmptyWhenFindByAccountNumberIsNull behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
    @Test
    void shouldReturnEmptyWhenFindByAccountNumberIsNull() {
        Optional<Account> result = accountRepository.findByAccountNumber(null);

        assertThat(result).isEmpty();
    }

    /*
     * Test Case: shouldReturnFalseWhenExistsByAccountNumberIsNull
     * Description: Validates shouldReturnFalseWhenExistsByAccountNumberIsNull behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
    @Test
    void shouldReturnFalseWhenExistsByAccountNumberIsNull() {
        boolean exists = accountRepository.existsByAccountNumber(null);

        assertThat(exists).isFalse();
    }

    /*
     * Test Case: shouldReturnEmptyWhenFindByAccountNumberIsBlank
     * Description: Validates shouldReturnEmptyWhenFindByAccountNumberIsBlank behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
    @Test
    void shouldReturnEmptyWhenFindByAccountNumberIsBlank() {
        Optional<Account> result = accountRepository.findByAccountNumber("");

        assertThat(result).isEmpty();
    }

    /*
     * Test Case: shouldReturnEmptyWhenFindByAccountNumberIsWhitespace
     * Description: Validates shouldReturnEmptyWhenFindByAccountNumberIsWhitespace behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
    @Test
    void shouldReturnEmptyWhenFindByAccountNumberIsWhitespace() {
        Optional<Account> result = accountRepository.findByAccountNumber(" ");

        assertThat(result).isEmpty();
    }

    /*
     * Test Case: shouldTrackRepositoryCountAfterSaveAndDelete
     * Description: Validates shouldTrackRepositoryCountAfterSaveAndDelete behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
    @Test
    void shouldTrackRepositoryCountAfterSaveAndDelete() {
        Customer savedCustomer = persistCustomer("John Doe", "john@example.com", "9876543210");
        long initialCount = accountRepository.count();

        Account savedAccount = accountRepository.saveAndFlush(
                newAccount("SB100001", "SAVINGS", new BigDecimal("5000.00"), savedCustomer));
        long countAfterSave = accountRepository.count();

        accountRepository.delete(savedAccount);
        accountRepository.flush();
        long countAfterDelete = accountRepository.count();

        assertThat(countAfterSave).isEqualTo(initialCount + 1);
        assertThat(countAfterDelete).isEqualTo(initialCount);
    }

    private Customer persistCustomer(String name, String email, String phone) {
        Customer customer = new Customer();
        customer.setName(name != null ? name : baseCustomer.getName());
        customer.setEmail(email != null ? email : baseCustomer.getEmail());
        customer.setPhone(phone != null ? phone : baseCustomer.getPhone());
        return testEntityManager.persistFlushFind(customer);
    }

    private Account newAccount(String accountNumber, String accountType, BigDecimal balance, Customer customer) {
        Account account = new Account();
        account.setAccountNumber(accountNumber != null ? accountNumber : baseAccount.getAccountNumber());
        account.setAccountType(accountType != null ? accountType : baseAccount.getAccountType());
        account.setBalance(balance != null ? balance : baseAccount.getBalance());
        account.setCustomer(customer);
        return account;
    }
}
