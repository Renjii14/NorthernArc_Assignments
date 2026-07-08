package org.northernarc.week5_assess.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.northernarc.week5_assess.entity.Account;
import org.northernarc.week5_assess.entity.Transaction;
import org.northernarc.week5_assess.exception.AccountNotFoundException;
import org.northernarc.week5_assess.exception.GlobalExceptionHandler;
import org.northernarc.week5_assess.exception.TransactionNotFoundException;
import org.northernarc.week5_assess.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TransactionController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
@DisplayName("Transaction controller API behaviour")
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
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

    @Nested
    class GetAllTransactions {

    /*
     * Test Case: shouldReturnAllTransactions
     * Description: Validates shouldReturnAllTransactions behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnAllTransactions() throws Exception {
            when(transactionService.getAllTransactions())
                    .thenReturn(List.of(transaction(1L, 10L, "100.00"), transaction(2L, 10L, "50.00")));

            mockMvc.perform(get("/api/transactions"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                    .andExpect(jsonPath("$[0].id").value(1L))
                    .andExpect(jsonPath("$[0].amount").value(100.00))
                    .andExpect(jsonPath("$[1].id").value(2L));

            verify(transactionService).getAllTransactions();
        }

    /*
     * Test Case: shouldReturnEmptyTransactionList
     * Description: Validates shouldReturnEmptyTransactionList behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnEmptyTransactionList() throws Exception {
            when(transactionService.getAllTransactions()).thenReturn(List.of());

            mockMvc.perform(get("/api/transactions"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                    .andExpect(content().json("[]"));

            verify(transactionService).getAllTransactions();
        }
    }

    @Nested
    class GetTransactionById {

    /*
     * Test Case: shouldReturnTransactionWhenIdExists
     * Description: Validates shouldReturnTransactionWhenIdExists behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnTransactionWhenIdExists() throws Exception {
            when(transactionService.getTransactionById(1L)).thenReturn(transaction(1L, 10L, "250.00"));

            mockMvc.perform(get("/api/transactions/{id}", 1L))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.amount").value(250.00));

            verify(transactionService).getTransactionById(1L);
        }

    /*
     * Test Case: shouldReturnNotFoundWhenTransactionMissing
     * Description: Validates shouldReturnNotFoundWhenTransactionMissing behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnNotFoundWhenTransactionMissing() throws Exception {
            when(transactionService.getTransactionById(99L)).thenThrow(new TransactionNotFoundException("not found"));

            mockMvc.perform(get("/api/transactions/{id}", 99L))
                    .andExpect(status().isNotFound());

            verify(transactionService).getTransactionById(99L);
        }

    /*
     * Test Case: shouldReturnBadRequestWhenTransactionIdIsInvalid
     * Description: Validates shouldReturnBadRequestWhenTransactionIdIsInvalid behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnBadRequestWhenTransactionIdIsInvalid() throws Exception {
            mockMvc.perform(get("/api/transactions/{id}", "abc"))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(transactionService);
        }
    }

    @Nested
    class GetTransactionsByAccountId {

    /*
     * Test Case: shouldReturnExistingAccountTransactions
     * Description: Validates shouldReturnExistingAccountTransactions behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnExistingAccountTransactions() throws Exception {
            when(transactionService.getTransactionsByAccountId(10L))
                    .thenReturn(List.of(transaction(1L, 10L, "100.00"), transaction(2L, 10L, "25.00")));

            mockMvc.perform(get("/api/accounts/{id}/transactions", 10L))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                    .andExpect(jsonPath("$[0].account.id").value(10L))
                    .andExpect(jsonPath("$[1].amount").value(25.00));

            verify(transactionService).getTransactionsByAccountId(10L);
        }

    /*
     * Test Case: shouldReturnEmptyListWhenAccountHasNoTransactions
     * Description: Validates shouldReturnEmptyListWhenAccountHasNoTransactions behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnEmptyListWhenAccountHasNoTransactions() throws Exception {
            when(transactionService.getTransactionsByAccountId(11L)).thenReturn(List.of());

            mockMvc.perform(get("/api/accounts/{id}/transactions", 11L))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                    .andExpect(content().json("[]"));

            verify(transactionService).getTransactionsByAccountId(11L);
        }

    /*
     * Test Case: shouldReturnNotFoundWhenAccountUnknown
     * Description: Validates shouldReturnNotFoundWhenAccountUnknown behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnNotFoundWhenAccountUnknown() throws Exception {
            when(transactionService.getTransactionsByAccountId(999L))
                    .thenThrow(new AccountNotFoundException("account not found"));

            mockMvc.perform(get("/api/accounts/{id}/transactions", 999L))
                    .andExpect(status().isNotFound());

            verify(transactionService).getTransactionsByAccountId(999L);
        }

    /*
     * Test Case: shouldReturnBadRequestWhenAccountIdInvalid
     * Description: Validates shouldReturnBadRequestWhenAccountIdInvalid behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnBadRequestWhenAccountIdInvalid() throws Exception {
            mockMvc.perform(get("/api/accounts/{id}/transactions", "bad"))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(transactionService);
        }
    }

    private Transaction transaction(Long id, Long accountId, String amount) {
        Transaction transaction = new Transaction();
        transaction.setId(id != null ? id : baseTransaction.getId());
        transaction.setAmount(new BigDecimal(amount != null ? amount : baseTransaction.getAmount().toPlainString()));
        transaction.setType(baseTransaction.getType());
        transaction.setCreatedAt(LocalDateTime.now());
        Account account = new Account();
        account.setId(accountId != null ? accountId : baseTransaction.getAccount().getId());
        transaction.setAccount(account);
        return transaction;
    }
}
