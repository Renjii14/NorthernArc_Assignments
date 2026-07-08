package org.northernarc.week5_assess.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.northernarc.week5_assess.dto.AccountCreateRequest;
import org.northernarc.week5_assess.entity.Account;
import org.northernarc.week5_assess.entity.Customer;
import org.northernarc.week5_assess.entity.Transaction;
import org.northernarc.week5_assess.exception.AccountNotFoundException;
import org.northernarc.week5_assess.exception.CustomerNotFoundException;
import org.northernarc.week5_assess.exception.DuplicateAccountNumberException;
import org.northernarc.week5_assess.exception.InsufficientBalanceException;
import org.northernarc.week5_assess.repository.AccountRepository;
import org.northernarc.week5_assess.repository.CustomerRepository;
import org.northernarc.week5_assess.repository.TransactionRepository;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private AccountService accountService;

    private Customer baseCustomer;
    private Account baseAccount;

    @BeforeEach
    void beforeEach(TestInfo testInfo) {
        baseCustomer = new Customer();
        baseCustomer.setId(1L);
        baseCustomer.setName("John Doe");
        baseCustomer.setEmail("john@example.com");
        baseCustomer.setPhone("9876543210");

        baseAccount = new Account();
        baseAccount.setId(1L);
        baseAccount.setAccountNumber("SB100001");
        baseAccount.setAccountType("SAVINGS");
        baseAccount.setBalance(new BigDecimal("1000.00"));
        baseAccount.setCustomer(baseCustomer);

        System.out.println("Running test: " + testInfo.getDisplayName());
    }

    @Nested
    class AccountCreation {

    /*
     * Test Case: shouldCreateAccountSuccessfully
     * Description: Validates shouldCreateAccountSuccessfully behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldCreateAccountSuccessfully() {
            AccountCreateRequest request = validCreateRequest("SB100001", "SAVINGS", "5000.00", 1L);
            Customer customer = customer(1L);
            when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
            when(accountRepository.existsByAccountNumber("SB100001")).thenReturn(false);
            when(accountRepository.save(isA(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));

            Account createdAccount = accountService.createAccount(request);

            assertThat(createdAccount.getAccountNumber()).isEqualTo("SB100001");
            assertThat(createdAccount.getBalance()).isEqualByComparingTo("5000.00");
            verify(customerRepository).findById(1L);
            verify(accountRepository).existsByAccountNumber("SB100001");
            verify(accountRepository).save(any(Account.class));
        }

    /*
     * Test Case: shouldThrowCustomerNotFoundExceptionWhenCustomerDoesNotExist
     * Description: Validates shouldThrowCustomerNotFoundExceptionWhenCustomerDoesNotExist behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldThrowCustomerNotFoundExceptionWhenCustomerDoesNotExist() {
            AccountCreateRequest request = validCreateRequest("SB100001", "SAVINGS", "1000.00", 44L);
            when(customerRepository.findById(44L)).thenReturn(Optional.empty());

            assertThrows(CustomerNotFoundException.class, () -> accountService.createAccount(request));

            verify(customerRepository).findById(44L);
            verify(accountRepository, never()).save(any(Account.class));
        }

    /*
     * Test Case: shouldAllowZeroOpeningBalance
     * Description: Validates shouldAllowZeroOpeningBalance behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldAllowZeroOpeningBalance() {
            AccountCreateRequest request = validCreateRequest("SB100002", "SAVINGS", "0.00", 1L);
            when(customerRepository.findById(1L)).thenReturn(Optional.of(customer(1L)));
            when(accountRepository.existsByAccountNumber("SB100002")).thenReturn(false);
            when(accountRepository.save(isA(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));

            Account createdAccount = accountService.createAccount(request);

            assertThat(createdAccount.getBalance()).isEqualByComparingTo("0.00");
            verify(accountRepository).save(any(Account.class));
        }

    /*
     * Test Case: shouldAllowPositiveOpeningBalance
     * Description: Validates shouldAllowPositiveOpeningBalance behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldAllowPositiveOpeningBalance() {
            AccountCreateRequest request = validCreateRequest("SB100003", "CURRENT", "200.50", 1L);
            when(customerRepository.findById(1L)).thenReturn(Optional.of(customer(1L)));
            when(accountRepository.existsByAccountNumber("SB100003")).thenReturn(false);
            when(accountRepository.save(isA(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));

            Account createdAccount = accountService.createAccount(request);

            assertThat(createdAccount.getBalance()).isEqualByComparingTo("200.50");
        }

    /*
     * Test Case: shouldRejectNegativeOpeningBalance
     * Description: Validates shouldRejectNegativeOpeningBalance behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldRejectNegativeOpeningBalance() {
            AccountCreateRequest request = validCreateRequest("SB100004", "SAVINGS", "-1.00", 1L);

            assertThrows(IllegalArgumentException.class, () -> accountService.createAccount(request));

            verify(accountRepository, never()).save(any(Account.class));
        }

    /*
     * Test Case: shouldThrowExceptionWhenDuplicateAccountNumberExists
     * Description: Validates shouldThrowExceptionWhenDuplicateAccountNumberExists behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldThrowExceptionWhenDuplicateAccountNumberExists() {
            AccountCreateRequest request = validCreateRequest("SB100001", "SAVINGS", "100.00", 1L);
            when(customerRepository.findById(1L)).thenReturn(Optional.of(customer(1L)));
            when(accountRepository.existsByAccountNumber("SB100001")).thenReturn(true);

            assertThrows(DuplicateAccountNumberException.class, () -> accountService.createAccount(request));

            verify(accountRepository, never()).save(any(Account.class));
        }

    /*
     * Test Case: shouldFailWhenAccountNumberIsBlank
     * Description: Validates shouldFailWhenAccountNumberIsBlank behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldFailWhenAccountNumberIsBlank() {
            AccountCreateRequest request = validCreateRequest(" ", "SAVINGS", "100.00", 1L);

            assertThrows(IllegalArgumentException.class, () -> accountService.createAccount(request));

            verify(accountRepository, never()).save(any(Account.class));
        }

    /*
     * Test Case: shouldFailWhenAccountNumberIsNull
     * Description: Validates shouldFailWhenAccountNumberIsNull behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldFailWhenAccountNumberIsNull() {
            AccountCreateRequest request = validCreateRequest(null, "SAVINGS", "100.00", 1L);

            assertThrows(IllegalArgumentException.class, () -> accountService.createAccount(request));

            verify(accountRepository, never()).save(any(Account.class));
        }

    /*
     * Test Case: shouldAcceptSavingsAccountType
     * Description: Validates shouldAcceptSavingsAccountType behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldAcceptSavingsAccountType() {
            AccountCreateRequest request = validCreateRequest("SB200001", "SAVINGS", "1000.00", 1L);
            when(customerRepository.findById(1L)).thenReturn(Optional.of(customer(1L)));
            when(accountRepository.existsByAccountNumber("SB200001")).thenReturn(false);
            when(accountRepository.save(isA(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));

            Account created = accountService.createAccount(request);

            assertThat(created.getAccountType()).isEqualTo("SAVINGS");
        }

    /*
     * Test Case: shouldAcceptCurrentAccountType
     * Description: Validates shouldAcceptCurrentAccountType behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldAcceptCurrentAccountType() {
            AccountCreateRequest request = validCreateRequest("CA200001", "CURRENT", "1000.00", 1L);
            when(customerRepository.findById(1L)).thenReturn(Optional.of(customer(1L)));
            when(accountRepository.existsByAccountNumber("CA200001")).thenReturn(false);
            when(accountRepository.save(isA(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));

            Account created = accountService.createAccount(request);

            assertThat(created.getAccountType()).isEqualTo("CURRENT");
        }

    /*
     * Test Case: shouldRejectInvalidAccountType
     * Description: Validates shouldRejectInvalidAccountType behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldRejectInvalidAccountType() {
            AccountCreateRequest request = validCreateRequest("XX200001", "SALARY", "1000.00", 1L);

            assertThrows(IllegalArgumentException.class, () -> accountService.createAccount(request));

            verify(accountRepository, never()).save(any(Account.class));
        }

    /*
     * Test Case: shouldRejectNullAccountType
     * Description: Validates shouldRejectNullAccountType behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldRejectNullAccountType() {
            AccountCreateRequest request = validCreateRequest("XX200002", null, "1000.00", 1L);

            assertThrows(IllegalArgumentException.class, () -> accountService.createAccount(request));
        }

    /*
     * Test Case: shouldFailWhenCreateRequestIsNull
     * Description: Validates shouldFailWhenCreateRequestIsNull behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldFailWhenCreateRequestIsNull() {
            assertThrows(IllegalArgumentException.class, () -> accountService.createAccount(null));

            verifyNoMoreInteractions(accountRepository, customerRepository, transactionRepository);
        }
    }

    @Nested
    class Deposit {

    /*
     * Test Case: shouldDepositSuccessfully
     * Description: Validates shouldDepositSuccessfully behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldDepositSuccessfully() {
            Account account = account(10L, "SB100001", "SAVINGS", "1000.00");
            when(accountRepository.findById(10L)).thenReturn(Optional.of(account));
            when(accountRepository.save(isA(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));
            when(transactionRepository.save(isA(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

            Account result = accountService.deposit(10L, new BigDecimal("250.00"));

            assertThat(result.getBalance()).isEqualByComparingTo("1250.00");
            verify(accountRepository).findById(10L);
            verify(accountRepository).save(account);
            verify(transactionRepository).save(any(Transaction.class));
        }

    /*
     * Test Case: shouldDepositDecimalAmount
     * Description: Validates shouldDepositDecimalAmount behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldDepositDecimalAmount() {
            Account account = account(10L, "SB100001", "SAVINGS", "1000.10");
            when(accountRepository.findById(10L)).thenReturn(Optional.of(account));
            when(accountRepository.save(isA(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));
            when(transactionRepository.save(isA(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

            Account result = accountService.deposit(10L, new BigDecimal("0.25"));

            assertThat(result.getBalance()).isEqualByComparingTo("1000.35");
        }

    /*
     * Test Case: shouldFailWhenDepositAmountIsZero
     * Description: Validates shouldFailWhenDepositAmountIsZero behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldFailWhenDepositAmountIsZero() {
            assertThrows(IllegalArgumentException.class, () -> accountService.deposit(10L, BigDecimal.ZERO));

            verify(accountRepository, never()).save(any(Account.class));
            verify(transactionRepository, never()).save(any(Transaction.class));
        }

    /*
     * Test Case: shouldFailWhenDepositAmountIsNegative
     * Description: Validates shouldFailWhenDepositAmountIsNegative behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldFailWhenDepositAmountIsNegative() {
            assertThrows(IllegalArgumentException.class, () -> accountService.deposit(10L, new BigDecimal("-10.00")));

            verify(accountRepository, never()).save(any(Account.class));
            verify(transactionRepository, never()).save(any(Transaction.class));
        }

    /*
     * Test Case: shouldThrowAccountNotFoundExceptionWhenDepositAccountMissing
     * Description: Validates shouldThrowAccountNotFoundExceptionWhenDepositAccountMissing behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldThrowAccountNotFoundExceptionWhenDepositAccountMissing() {
            when(accountRepository.findById(999L)).thenReturn(Optional.empty());

            assertThrows(AccountNotFoundException.class, () -> accountService.deposit(999L, new BigDecimal("25.00")));

            verify(accountRepository).findById(999L);
            verify(accountRepository, never()).save(any(Account.class));
            verify(transactionRepository, never()).save(any(Transaction.class));
        }
    }

    @Nested
    class Withdrawal {

    /*
     * Test Case: shouldWithdrawSuccessfully
     * Description: Validates shouldWithdrawSuccessfully behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldWithdrawSuccessfully() {
            Account account = account(10L, "SB100001", "SAVINGS", "1000.00");
            when(accountRepository.findById(10L)).thenReturn(Optional.of(account));
            when(accountRepository.save(isA(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));
            when(transactionRepository.save(isA(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

            Account result = accountService.withdraw(10L, new BigDecimal("300.00"));

            assertThat(result.getBalance()).isEqualByComparingTo("700.00");
            verify(accountRepository).save(account);
            verify(transactionRepository).save(any(Transaction.class));
        }

    /*
     * Test Case: shouldWithdrawExactAvailableBalance
     * Description: Validates shouldWithdrawExactAvailableBalance behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldWithdrawExactAvailableBalance() {
            Account account = account(10L, "SB100001", "SAVINGS", "300.00");
            when(accountRepository.findById(10L)).thenReturn(Optional.of(account));
            when(accountRepository.save(isA(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));
            when(transactionRepository.save(isA(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

            Account result = accountService.withdraw(10L, new BigDecimal("300.00"));

            assertThat(result.getBalance()).isEqualByComparingTo("0.00");
        }

    /*
     * Test Case: shouldWithdrawPartialBalance
     * Description: Validates shouldWithdrawPartialBalance behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldWithdrawPartialBalance() {
            Account account = account(10L, "SB100001", "SAVINGS", "300.00");
            when(accountRepository.findById(10L)).thenReturn(Optional.of(account));
            when(accountRepository.save(isA(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));
            when(transactionRepository.save(isA(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

            Account result = accountService.withdraw(10L, new BigDecimal("120.00"));

            assertThat(result.getBalance()).isEqualByComparingTo("180.00");
        }

    /*
     * Test Case: shouldThrowInsufficientBalanceExceptionWhenAmountGreaterThanBalance
     * Description: Validates shouldThrowInsufficientBalanceExceptionWhenAmountGreaterThanBalance behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldThrowInsufficientBalanceExceptionWhenAmountGreaterThanBalance() {
            Account account = account(10L, "SB100001", "SAVINGS", "100.00");
            when(accountRepository.findById(10L)).thenReturn(Optional.of(account));

            assertThrows(InsufficientBalanceException.class, () -> accountService.withdraw(10L, new BigDecimal("101.00")));

            assertThat(account.getBalance()).isEqualByComparingTo("100.00");
            verify(accountRepository).findById(10L);
            verify(accountRepository, never()).save(any(Account.class));
            verify(transactionRepository, never()).save(any(Transaction.class));
        }

    /*
     * Test Case: shouldFailWhenWithdrawalAmountIsZero
     * Description: Validates shouldFailWhenWithdrawalAmountIsZero behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldFailWhenWithdrawalAmountIsZero() {
            assertThrows(IllegalArgumentException.class, () -> accountService.withdraw(10L, BigDecimal.ZERO));

            verify(accountRepository, never()).save(any(Account.class));
            verify(transactionRepository, never()).save(any(Transaction.class));
        }

    /*
     * Test Case: shouldFailWhenWithdrawalAmountIsNegative
     * Description: Validates shouldFailWhenWithdrawalAmountIsNegative behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldFailWhenWithdrawalAmountIsNegative() {
            assertThrows(IllegalArgumentException.class, () -> accountService.withdraw(10L, new BigDecimal("-1.00")));

            verify(accountRepository, never()).save(any(Account.class));
            verify(transactionRepository, never()).save(any(Transaction.class));
        }

    /*
     * Test Case: shouldThrowAccountNotFoundExceptionWhenWithdrawingFromNonExistingAccount
     * Description: Validates shouldThrowAccountNotFoundExceptionWhenWithdrawingFromNonExistingAccount behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldThrowAccountNotFoundExceptionWhenWithdrawingFromNonExistingAccount() {
            when(accountRepository.findById(120L)).thenReturn(Optional.empty());

            assertThrows(AccountNotFoundException.class, () -> accountService.withdraw(120L, new BigDecimal("100.00")));

            verify(accountRepository).findById(120L);
            verify(accountRepository, never()).save(any(Account.class));
            verify(transactionRepository, never()).save(any(Transaction.class));
        }
    }

    @Nested
    class Transfer {

    /*
     * Test Case: shouldTransferSuccessfully
     * Description: Validates shouldTransferSuccessfully behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldTransferSuccessfully() {
            Account source = account(1L, "SB100001", "SAVINGS", "1000.00");
            Account destination = account(2L, "CA100001", "CURRENT", "500.00");
            when(accountRepository.findById(1L)).thenReturn(Optional.of(source));
            when(accountRepository.findById(2L)).thenReturn(Optional.of(destination));
            when(accountRepository.save(isA(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));
            when(transactionRepository.save(isA(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

            accountService.transfer(1L, 2L, new BigDecimal("250.00"));

            assertThat(source.getBalance()).isEqualByComparingTo("750.00");
            assertThat(destination.getBalance()).isEqualByComparingTo("750.00");
            verify(accountRepository).findById(1L);
            verify(accountRepository).findById(2L);
            verify(accountRepository, times(2)).save(any(Account.class));
            verify(transactionRepository, times(2)).save(any(Transaction.class));
        }

    /*
     * Test Case: shouldThrowAccountNotFoundWhenSourceAccountNotFound
     * Description: Validates shouldThrowAccountNotFoundWhenSourceAccountNotFound behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldThrowAccountNotFoundWhenSourceAccountNotFound() {
            when(accountRepository.findById(1L)).thenReturn(Optional.empty());

            assertThrows(AccountNotFoundException.class, () -> accountService.transfer(1L, 2L, new BigDecimal("50.00")));

            verify(accountRepository).findById(1L);
            verify(transactionRepository, never()).save(any(Transaction.class));
        }

    /*
     * Test Case: shouldThrowAccountNotFoundWhenDestinationAccountNotFound
     * Description: Validates shouldThrowAccountNotFoundWhenDestinationAccountNotFound behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldThrowAccountNotFoundWhenDestinationAccountNotFound() {
            when(accountRepository.findById(1L)).thenReturn(Optional.of(account(1L, "SB100001", "SAVINGS", "200.00")));
            when(accountRepository.findById(2L)).thenReturn(Optional.empty());

            assertThrows(AccountNotFoundException.class, () -> accountService.transfer(1L, 2L, new BigDecimal("50.00")));

            verify(accountRepository).findById(1L);
            verify(accountRepository).findById(2L);
            verify(transactionRepository, never()).save(any(Transaction.class));
        }

    /*
     * Test Case: shouldFailWhenSourceAndDestinationAreSame
     * Description: Validates shouldFailWhenSourceAndDestinationAreSame behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldFailWhenSourceAndDestinationAreSame() {
            assertThrows(IllegalArgumentException.class, () -> accountService.transfer(10L, 10L, new BigDecimal("50.00")));

            verifyNoMoreInteractions(accountRepository, transactionRepository);
        }

    /*
     * Test Case: shouldFailWhenTransferAmountIsZero
     * Description: Validates shouldFailWhenTransferAmountIsZero behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldFailWhenTransferAmountIsZero() {
            assertThrows(IllegalArgumentException.class, () -> accountService.transfer(1L, 2L, BigDecimal.ZERO));

            verify(transactionRepository, never()).save(any(Transaction.class));
        }

    /*
     * Test Case: shouldFailWhenTransferAmountIsNegative
     * Description: Validates shouldFailWhenTransferAmountIsNegative behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldFailWhenTransferAmountIsNegative() {
            assertThrows(IllegalArgumentException.class, () -> accountService.transfer(1L, 2L, new BigDecimal("-5.00")));

            verify(transactionRepository, never()).save(any(Transaction.class));
        }

    /*
     * Test Case: shouldTransferExactAvailableBalance
     * Description: Validates shouldTransferExactAvailableBalance behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldTransferExactAvailableBalance() {
            Account source = account(1L, "SB100001", "SAVINGS", "300.00");
            Account destination = account(2L, "CA100001", "CURRENT", "50.00");
            when(accountRepository.findById(1L)).thenReturn(Optional.of(source));
            when(accountRepository.findById(2L)).thenReturn(Optional.of(destination));
            when(accountRepository.save(isA(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));
            when(transactionRepository.save(isA(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

            accountService.transfer(1L, 2L, new BigDecimal("300.00"));

            assertThat(source.getBalance()).isEqualByComparingTo("0.00");
            assertThat(destination.getBalance()).isEqualByComparingTo("350.00");
        }

    /*
     * Test Case: shouldFailWhenTransferAmountGreaterThanBalance
     * Description: Validates shouldFailWhenTransferAmountGreaterThanBalance behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldFailWhenTransferAmountGreaterThanBalance() {
            Account source = account(1L, "SB100001", "SAVINGS", "300.00");
            Account destination = account(2L, "CA100001", "CURRENT", "50.00");
            when(accountRepository.findById(1L)).thenReturn(Optional.of(source));
            when(accountRepository.findById(2L)).thenReturn(Optional.of(destination));

            assertThrows(InsufficientBalanceException.class, () -> accountService.transfer(1L, 2L, new BigDecimal("301.00")));

            assertThat(source.getBalance()).isEqualByComparingTo("300.00");
            assertThat(destination.getBalance()).isEqualByComparingTo("50.00");
            verify(accountRepository, never()).save(any(Account.class));
            verify(transactionRepository, never()).save(any(Transaction.class));
        }

    /*
     * Test Case: shouldRollbackWhenDestinationSaveFails
     * Description: Validates shouldRollbackWhenDestinationSaveFails behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldRollbackWhenDestinationSaveFails() {
            Account source = account(1L, "SB100001", "SAVINGS", "600.00");
            Account destination = account(2L, "CA100001", "CURRENT", "100.00");
            RuntimeException dbFailure = new RuntimeException("destination save failed");
            when(accountRepository.findById(1L)).thenReturn(Optional.of(source));
            when(accountRepository.findById(2L)).thenReturn(Optional.of(destination));
            when(accountRepository.save(eq(source))).thenThrow(dbFailure);

            RuntimeException thrown = assertThrows(RuntimeException.class, () -> accountService.transfer(1L, 2L, new BigDecimal("100.00")));
            assertThat(thrown).isSameAs(dbFailure);

            assertThat(source.getBalance()).isEqualByComparingTo("600.00");
            assertThat(destination.getBalance()).isEqualByComparingTo("100.00");
            verify(transactionRepository, never()).save(any(Transaction.class));
        }

    /*
     * Test Case: shouldRollbackWhenTransactionSaveFails
     * Description: Validates shouldRollbackWhenTransactionSaveFails behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldRollbackWhenTransactionSaveFails() {
            Account source = account(1L, "SB100001", "SAVINGS", "500.00");
            Account destination = account(2L, "CA100001", "CURRENT", "100.00");
            RuntimeException txFailure = new RuntimeException("tx save failed");
            when(accountRepository.findById(1L)).thenReturn(Optional.of(source));
            when(accountRepository.findById(2L)).thenReturn(Optional.of(destination));
            when(accountRepository.save(isA(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));
            when(transactionRepository.save(isA(Transaction.class))).thenThrow(txFailure);

            RuntimeException thrown = assertThrows(RuntimeException.class, () -> accountService.transfer(1L, 2L, new BigDecimal("100.00")));
            assertThat(thrown).isSameAs(txFailure);

            assertThat(source.getBalance()).isEqualByComparingTo("500.00");
            assertThat(destination.getBalance()).isEqualByComparingTo("100.00");
            verify(accountRepository, times(2)).findById(any(Long.class));
            verify(transactionRepository).save(any(Transaction.class));
        }

    /*
     * Test Case: shouldCreateTwoTransactionRecordsForSuccessfulTransfer
     * Description: Validates shouldCreateTwoTransactionRecordsForSuccessfulTransfer behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldCreateTwoTransactionRecordsForSuccessfulTransfer() {
            Account source = account(1L, "SB100001", "SAVINGS", "900.00");
            Account destination = account(2L, "CA100001", "CURRENT", "200.00");
            ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
            when(accountRepository.findById(1L)).thenReturn(Optional.of(source));
            when(accountRepository.findById(2L)).thenReturn(Optional.of(destination));
            when(accountRepository.save(isA(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));
            when(transactionRepository.save(isA(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

            accountService.transfer(1L, 2L, new BigDecimal("100.00"));

            verify(transactionRepository, times(2)).save(transactionCaptor.capture());
            assertThat(transactionCaptor.getAllValues()).hasSize(2);
        }
    }

    private AccountCreateRequest validCreateRequest(String accountNumber, String accountType, String openingBalance,
            Long customerId) {
        AccountCreateRequest request = new AccountCreateRequest();
        request.setAccountNumber(accountNumber);
        request.setAccountType(accountType);
        request.setOpeningBalance(new BigDecimal(openingBalance));
        request.setCustomerId(customerId);
        return request;
    }

    private Customer customer(Long id) {
        Customer customer = new Customer();
        customer.setId(id);
        customer.setName(baseCustomer.getName());
        customer.setEmail(baseCustomer.getEmail());
        customer.setPhone(baseCustomer.getPhone());
        return customer;
    }

    private Account account(Long id, String accountNumber, String accountType, String balance) {
        Account account = new Account();
        account.setId(id);
        account.setAccountNumber(accountNumber);
        account.setAccountType(accountType);
        account.setBalance(new BigDecimal(balance));
        account.setCustomer(customer(baseCustomer.getId()));
        return account;
    }
}
