package org.northernarc.week5_assess.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.northernarc.week5_assess.entity.Account;
import org.northernarc.week5_assess.entity.Transaction;
import org.northernarc.week5_assess.exception.TransactionNotFoundException;
import org.northernarc.week5_assess.repository.TransactionRepository;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    private Transaction baseTransaction;

    @BeforeEach
    void beforeEach(TestInfo testInfo) {
        baseTransaction = new Transaction();
        baseTransaction.setId(1L);
        baseTransaction.setAmount(new BigDecimal("100.00"));
        baseTransaction.setType("DEPOSIT");
        baseTransaction.setCreatedAt(LocalDateTime.now());
        Account account = new Account();
        account.setId(10L);
        baseTransaction.setAccount(account);

        System.out.println("Running test: " + testInfo.getDisplayName());
    }

    /*
     * Test Case: shouldGetAllTransactions
     * Description: Validates shouldGetAllTransactions behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
    @Test
    void shouldGetAllTransactions() {
        when(transactionRepository.findAll()).thenReturn(List.of(transaction(1L, "100.00"), transaction(2L, "200.00")));

        List<Transaction> result = transactionService.getAllTransactions();

        assertThat(result).hasSize(2);
        verify(transactionRepository).findAll();
        verifyNoMoreInteractions(transactionRepository);
    }

    /*
     * Test Case: shouldGetTransactionById
     * Description: Validates shouldGetTransactionById behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
    @Test
    void shouldGetTransactionById() {
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction(1L, "100.00")));

        Transaction result = transactionService.getTransactionById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        verify(transactionRepository).findById(1L);
    }

    /*
     * Test Case: shouldThrowWhenTransactionNotFound
     * Description: Validates shouldThrowWhenTransactionNotFound behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
    @Test
    void shouldThrowWhenTransactionNotFound() {
        when(transactionRepository.findById(404L)).thenReturn(Optional.empty());

        assertThrows(TransactionNotFoundException.class, () -> transactionService.getTransactionById(404L));

        verify(transactionRepository).findById(404L);
    }

    /*
     * Test Case: shouldGetTransactionsByAccountId
     * Description: Validates shouldGetTransactionsByAccountId behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
    @Test
    void shouldGetTransactionsByAccountId() {
        when(transactionRepository.findByAccountId(10L))
                .thenReturn(List.of(transaction(1L, "100.00"), transaction(2L, "250.00")));

        List<Transaction> result = transactionService.getTransactionsByAccountId(10L);

        assertThat(result).hasSize(2);
        verify(transactionRepository).findByAccountId(10L);
    }

    /*
     * Test Case: shouldReturnEmptyListWhenAccountHasNoTransactions
     * Description: Validates shouldReturnEmptyListWhenAccountHasNoTransactions behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
    @Test
    void shouldReturnEmptyListWhenAccountHasNoTransactions() {
        when(transactionRepository.findByAccountId(10L)).thenReturn(List.of());

        List<Transaction> result = transactionService.getTransactionsByAccountId(10L);

        assertThat(result).isEmpty();
        verify(transactionRepository).findByAccountId(10L);
    }

    /*
     * Test Case: shouldReturnTransactionsInExpectedOrder
     * Description: Validates shouldReturnTransactionsInExpectedOrder behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
    @Test
    void shouldReturnTransactionsInExpectedOrder() {
        Transaction latest = transaction(2L, "200.00");
        latest.setCreatedAt(LocalDateTime.now());
        Transaction older = transaction(1L, "100.00");
        older.setCreatedAt(LocalDateTime.now().minusDays(1));
        when(transactionRepository.findByAccountIdOrderByCreatedAtDesc(10L)).thenReturn(List.of(latest, older));

        List<Transaction> result = transactionService.getTransactionsByAccountId(10L);

        assertThat(result).extracting(Transaction::getId).containsExactly(2L, 1L);
        verify(transactionRepository).findByAccountIdOrderByCreatedAtDesc(10L);
    }

    /*
     * Test Case: shouldFailWhenTransactionIdInvalid
     * Description: Validates shouldFailWhenTransactionIdInvalid behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
    @Test
    void shouldFailWhenTransactionIdInvalid() {
        assertThrows(IllegalArgumentException.class, () -> transactionService.getTransactionById(0L));

        verify(transactionRepository, never()).findById(anyLong());
    }

    /*
     * Test Case: shouldFailWhenAccountIdInvalid
     * Description: Validates shouldFailWhenAccountIdInvalid behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
    @Test
    void shouldFailWhenAccountIdInvalid() {
        assertThrows(IllegalArgumentException.class, () -> transactionService.getTransactionsByAccountId(0L));

        verify(transactionRepository, never()).findByAccountId(anyLong());
    }

    /*
     * Test Case: shouldPropagateRepositoryException
     * Description: Validates shouldPropagateRepositoryException behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
    @Test
    void shouldPropagateRepositoryException() {
        RuntimeException failure = new RuntimeException("db read failure");
        when(transactionRepository.findAll()).thenThrow(failure);

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> transactionService.getAllTransactions());
        assertThat(thrown).isSameAs(failure);

        verify(transactionRepository).findAll();
    }

    private Transaction transaction(Long id, String amount) {
        Transaction transaction = new Transaction();
        transaction.setId(id);
        transaction.setAmount(new BigDecimal(amount));
        transaction.setType(baseTransaction.getType());
        Account account = new Account();
        account.setId(baseTransaction.getAccount().getId());
        transaction.setAccount(account);
        transaction.setCreatedAt(LocalDateTime.now());
        return transaction;
    }
}
