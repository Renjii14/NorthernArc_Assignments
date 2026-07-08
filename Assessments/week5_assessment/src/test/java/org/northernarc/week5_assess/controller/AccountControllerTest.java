package org.northernarc.week5_assess.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.northernarc.week5_assess.dto.AccountCreateRequest;
import org.northernarc.week5_assess.dto.AccountUpdateRequest;
import org.northernarc.week5_assess.entity.Account;
import org.northernarc.week5_assess.exception.*;
import org.northernarc.week5_assess.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(AccountController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
@DisplayName("Account controller API behaviour")
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AccountService accountService;

    private AccountCreateRequest baseCreateRequest;
    private AccountUpdateRequest baseUpdateRequest;
    private Account baseAccount;

    @BeforeEach
    void beforeEach(TestInfo testInfo) {
        baseCreateRequest = new AccountCreateRequest();
        baseCreateRequest.setCustomerId(1L);
        baseCreateRequest.setAccountNumber("SB100001");
        baseCreateRequest.setAccountType("SAVINGS");
        baseCreateRequest.setOpeningBalance(new BigDecimal("1000.00"));

        baseUpdateRequest = new AccountUpdateRequest();
        baseUpdateRequest.setAccountType("CURRENT");
        baseUpdateRequest.setAccountNumber("SB100001");

        baseAccount = new Account();
        baseAccount.setId(1L);
        baseAccount.setAccountNumber("SB100001");
        baseAccount.setAccountType("SAVINGS");
        baseAccount.setBalance(new BigDecimal("1000.00"));

        System.out.println("Running test: " + testInfo.getDisplayName());
    }

    @Nested
    class CreateAccount {

    /*
     * Test Case: shouldCreateAccountSuccessfully
     * Description: Validates shouldCreateAccountSuccessfully behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldCreateAccountSuccessfully() throws Exception {
            when(accountService.createAccount(isA(AccountCreateRequest.class)))
                    .thenReturn(account(1L, "SB100001", "SAVINGS", "1000.00"));

            mockMvc.perform(post("/api/accounts")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validCreateRequest())))
                    .andExpect(status().isCreated())
                    .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.accountNumber").value("SB100001"))
                    .andExpect(jsonPath("$.accountType").value("SAVINGS"));

            verify(accountService).createAccount(any(AccountCreateRequest.class));
        }

    /*
     * Test Case: shouldReturnConflictWhenDuplicateAccountNumber
     * Description: Validates shouldReturnConflictWhenDuplicateAccountNumber behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnConflictWhenDuplicateAccountNumber() throws Exception {
            when(accountService.createAccount(isA(AccountCreateRequest.class)))
                    .thenThrow(new DuplicateAccountNumberException("duplicate"));

            mockMvc.perform(post("/api/accounts")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validCreateRequest())))
                    .andExpect(status().isConflict());

            verify(accountService).createAccount(any(AccountCreateRequest.class));
        }

    /*
     * Test Case: shouldReturnNotFoundWhenCustomerMissing
     * Description: Validates shouldReturnNotFoundWhenCustomerMissing behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnNotFoundWhenCustomerMissing() throws Exception {
            when(accountService.createAccount(isA(AccountCreateRequest.class)))
                    .thenThrow(new CustomerNotFoundException("not found"));

            mockMvc.perform(post("/api/accounts")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validCreateRequest())))
                    .andExpect(status().isNotFound());

            verify(accountService).createAccount(any(AccountCreateRequest.class));
        }

    /*
     * Test Case: shouldReturnBadRequestForNegativeOpeningBalance
     * Description: Validates shouldReturnBadRequestForNegativeOpeningBalance behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnBadRequestForNegativeOpeningBalance() throws Exception {
            AccountCreateRequest request = validCreateRequest();
            request.setOpeningBalance(new BigDecimal("-1.00"));

            mockMvc.perform(post("/api/accounts")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(accountService);
        }

    /*
     * Test Case: shouldReturnBadRequestForInvalidAccountType
     * Description: Validates shouldReturnBadRequestForInvalidAccountType behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnBadRequestForInvalidAccountType() throws Exception {
            AccountCreateRequest request = validCreateRequest();
            request.setAccountType("SALARY");

            mockMvc.perform(post("/api/accounts")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(accountService);
        }

    /*
     * Test Case: shouldReturnBadRequestForMissingBody
     * Description: Validates shouldReturnBadRequestForMissingBody behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnBadRequestForMissingBody() throws Exception {
            mockMvc.perform(post("/api/accounts")
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(accountService);
        }

    /*
     * Test Case: shouldReturnBadRequestForMalformedJson
     * Description: Validates shouldReturnBadRequestForMalformedJson behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnBadRequestForMalformedJson() throws Exception {
            mockMvc.perform(post("/api/accounts")
                            .contentType(APPLICATION_JSON)
                            .content("{\"accountNumber\":"))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(accountService);
        }

    /*
     * Test Case: shouldReturnUnsupportedMediaTypeForCreateAccount
     * Description: Validates shouldReturnUnsupportedMediaTypeForCreateAccount behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnUnsupportedMediaTypeForCreateAccount() throws Exception {
            mockMvc.perform(post("/api/accounts")
                            .contentType(MediaType.TEXT_PLAIN)
                            .content("invalid"))
                    .andExpect(status().isUnsupportedMediaType());

            verifyNoInteractions(accountService);
        }
    }

    @Nested
    class QueryAccounts {

    /*
     * Test Case: shouldReturnAllAccounts
     * Description: Validates shouldReturnAllAccounts behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnAllAccounts() throws Exception {
            when(accountService.getAllAccounts()).thenReturn(List.of(account(1L, "SB100001", "SAVINGS", "1000.00")));

            mockMvc.perform(get("/api/accounts"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                    .andExpect(jsonPath("$[0].id").value(1L))
                    .andExpect(jsonPath("$[0].accountNumber").value("SB100001"));

            verify(accountService).getAllAccounts();
        }

    /*
     * Test Case: shouldReturnEmptyArrayWhenNoAccounts
     * Description: Validates shouldReturnEmptyArrayWhenNoAccounts behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnEmptyArrayWhenNoAccounts() throws Exception {
            when(accountService.getAllAccounts()).thenReturn(List.of());

            mockMvc.perform(get("/api/accounts"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                    .andExpect(content().json("[]"));

            verify(accountService).getAllAccounts();
        }

    /*
     * Test Case: shouldReturnAccountById
     * Description: Validates shouldReturnAccountById behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnAccountById() throws Exception {
            when(accountService.getAccountById(1L)).thenReturn(account(1L, "SB100001", "SAVINGS", "1000.00"));

            mockMvc.perform(get("/api/accounts/{id}", 1L))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.accountNumber").value("SB100001"));

            verify(accountService).getAccountById(1L);
        }

    /*
     * Test Case: shouldReturnNotFoundWhenAccountUnknown
     * Description: Validates shouldReturnNotFoundWhenAccountUnknown behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnNotFoundWhenAccountUnknown() throws Exception {
            when(accountService.getAccountById(99L)).thenThrow(new AccountNotFoundException("not found"));

            mockMvc.perform(get("/api/accounts/{id}", 99L))
                    .andExpect(status().isNotFound());

            verify(accountService).getAccountById(99L);
        }

    /*
     * Test Case: shouldReturnBadRequestForInvalidAccountId
     * Description: Validates shouldReturnBadRequestForInvalidAccountId behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnBadRequestForInvalidAccountId() throws Exception {
            mockMvc.perform(get("/api/accounts/{id}", "bad"))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(accountService);
        }
    }

    @Nested
    class UpdateAccount {

    /*
     * Test Case: shouldUpdateAccountSuccessfully
     * Description: Validates shouldUpdateAccountSuccessfully behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldUpdateAccountSuccessfully() throws Exception {
            when(accountService.updateAccount(eq(1L), isA(AccountUpdateRequest.class)))
                    .thenReturn(account(1L, "SB100001", "CURRENT", "2000.00"));

            mockMvc.perform(put("/api/accounts/{id}", 1L)
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validUpdateRequest())))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                    .andExpect(jsonPath("$.accountType").value("CURRENT"));

            verify(accountService).updateAccount(eq(1L), any(AccountUpdateRequest.class));
        }

    /*
     * Test Case: shouldReturnNotFoundForUnknownAccountOnUpdate
     * Description: Validates shouldReturnNotFoundForUnknownAccountOnUpdate behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnNotFoundForUnknownAccountOnUpdate() throws Exception {
            when(accountService.updateAccount(eq(99L), isA(AccountUpdateRequest.class)))
                    .thenThrow(new AccountNotFoundException("not found"));

            mockMvc.perform(put("/api/accounts/{id}", 99L)
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validUpdateRequest())))
                    .andExpect(status().isNotFound());

            verify(accountService).updateAccount(eq(99L), any(AccountUpdateRequest.class));
        }

    /*
     * Test Case: shouldReturnBadRequestForInvalidUpdateRequest
     * Description: Validates shouldReturnBadRequestForInvalidUpdateRequest behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnBadRequestForInvalidUpdateRequest() throws Exception {
            AccountUpdateRequest request = validUpdateRequest();
            request.setAccountType("INVALID");

            mockMvc.perform(put("/api/accounts/{id}", 1L)
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(accountService);
        }

    /*
     * Test Case: shouldReturnConflictForDuplicateAccountNumberOnUpdate
     * Description: Validates shouldReturnConflictForDuplicateAccountNumberOnUpdate behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnConflictForDuplicateAccountNumberOnUpdate() throws Exception {
            when(accountService.updateAccount(eq(1L), isA(AccountUpdateRequest.class)))
                    .thenThrow(new DuplicateAccountNumberException("duplicate"));

            mockMvc.perform(put("/api/accounts/{id}", 1L)
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validUpdateRequest())))
                    .andExpect(status().isConflict());

            verify(accountService).updateAccount(eq(1L), any(AccountUpdateRequest.class));
        }

    /*
     * Test Case: shouldReturnBadRequestForInvalidPathVariableOnUpdate
     * Description: Validates shouldReturnBadRequestForInvalidPathVariableOnUpdate behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnBadRequestForInvalidPathVariableOnUpdate() throws Exception {
            mockMvc.perform(put("/api/accounts/{id}", "bad")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validUpdateRequest())))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(accountService);
        }
    }

    @Nested
    class DeleteAccount {

    /*
     * Test Case: shouldDeleteAccountSuccessfully
     * Description: Validates shouldDeleteAccountSuccessfully behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldDeleteAccountSuccessfully() throws Exception {
            mockMvc.perform(delete("/api/accounts/{id}", 1L))
                    .andExpect(status().isNoContent());

            verify(accountService).deleteAccount(1L);
        }

    /*
     * Test Case: shouldReturnNotFoundWhenDeletingUnknownAccount
     * Description: Validates shouldReturnNotFoundWhenDeletingUnknownAccount behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnNotFoundWhenDeletingUnknownAccount() throws Exception {
            org.mockito.Mockito.doThrow(new AccountNotFoundException("not found"))
                    .when(accountService).deleteAccount(99L);

            mockMvc.perform(delete("/api/accounts/{id}", 99L))
                    .andExpect(status().isNotFound());

            verify(accountService).deleteAccount(99L);
        }

    /*
     * Test Case: shouldReturnBadRequestForInvalidDeleteAccountId
     * Description: Validates shouldReturnBadRequestForInvalidDeleteAccountId behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnBadRequestForInvalidDeleteAccountId() throws Exception {
            mockMvc.perform(delete("/api/accounts/{id}", "x"))
                    .andExpect(status().isBadRequest());

            verify(accountService, never()).deleteAccount(anyLong());
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
        void shouldDepositSuccessfully() throws Exception {
            when(accountService.deposit(eq(1L), isA(BigDecimal.class)))
                    .thenReturn(account(1L, "SB100001", "SAVINGS", "1200.00"));

            mockMvc.perform(post("/api/accounts/deposit")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(amountRequest(1L, "200.00"))))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                    .andExpect(jsonPath("$.balance").value(1200.00));

            verify(accountService).deposit(eq(1L), any(BigDecimal.class));
        }

    /*
     * Test Case: shouldReturnBadRequestForZeroDepositAmount
     * Description: Validates shouldReturnBadRequestForZeroDepositAmount behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnBadRequestForZeroDepositAmount() throws Exception {
            mockMvc.perform(post("/api/accounts/deposit")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(amountRequest(1L, "0.00"))))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(accountService);
        }

    /*
     * Test Case: shouldReturnBadRequestForNegativeDepositAmount
     * Description: Validates shouldReturnBadRequestForNegativeDepositAmount behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnBadRequestForNegativeDepositAmount() throws Exception {
            mockMvc.perform(post("/api/accounts/deposit")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(amountRequest(1L, "-10.00"))))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(accountService);
        }

    /*
     * Test Case: shouldReturnNotFoundForDepositUnknownAccount
     * Description: Validates shouldReturnNotFoundForDepositUnknownAccount behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnNotFoundForDepositUnknownAccount() throws Exception {
            when(accountService.deposit(eq(99L), isA(BigDecimal.class)))
                    .thenThrow(new AccountNotFoundException("not found"));

            mockMvc.perform(post("/api/accounts/deposit")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(amountRequest(99L, "10.00"))))
                    .andExpect(status().isNotFound());

            verify(accountService).deposit(eq(99L), any(BigDecimal.class));
        }

    /*
     * Test Case: shouldReturnBadRequestForMissingDepositBody
     * Description: Validates shouldReturnBadRequestForMissingDepositBody behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnBadRequestForMissingDepositBody() throws Exception {
            mockMvc.perform(post("/api/accounts/deposit")
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(accountService);
        }

    /*
     * Test Case: shouldReturnBadRequestForMalformedDepositJson
     * Description: Validates shouldReturnBadRequestForMalformedDepositJson behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnBadRequestForMalformedDepositJson() throws Exception {
            mockMvc.perform(post("/api/accounts/deposit")
                            .contentType(APPLICATION_JSON)
                            .content("{\"accountId\":"))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(accountService);
        }

    /*
     * Test Case: shouldReturnUnsupportedMediaTypeForDeposit
     * Description: Validates shouldReturnUnsupportedMediaTypeForDeposit behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnUnsupportedMediaTypeForDeposit() throws Exception {
            mockMvc.perform(post("/api/accounts/deposit")
                            .contentType(MediaType.TEXT_PLAIN)
                            .content("invalid"))
                    .andExpect(status().isUnsupportedMediaType());

            verifyNoInteractions(accountService);
        }
    }

    @Nested
    class Withdraw {

    /*
     * Test Case: shouldWithdrawSuccessfully
     * Description: Validates shouldWithdrawSuccessfully behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldWithdrawSuccessfully() throws Exception {
            when(accountService.withdraw(eq(1L), isA(BigDecimal.class)))
                    .thenReturn(account(1L, "SB100001", "SAVINGS", "800.00"));

            mockMvc.perform(post("/api/accounts/withdraw")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(amountRequest(1L, "200.00"))))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                    .andExpect(jsonPath("$.balance").value(800.00));

            verify(accountService).withdraw(eq(1L), any(BigDecimal.class));
        }

    /*
     * Test Case: shouldReturnBadRequestForInsufficientBalance
     * Description: Validates shouldReturnBadRequestForInsufficientBalance behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnBadRequestForInsufficientBalance() throws Exception {
            when(accountService.withdraw(eq(1L), isA(BigDecimal.class)))
                    .thenThrow(new InsufficientBalanceException("insufficient"));

            mockMvc.perform(post("/api/accounts/withdraw")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(amountRequest(1L, "5000.00"))))
                    .andExpect(status().isBadRequest());

            verify(accountService).withdraw(eq(1L), any(BigDecimal.class));
        }

    /*
     * Test Case: shouldReturnNotFoundForUnknownAccountOnWithdraw
     * Description: Validates shouldReturnNotFoundForUnknownAccountOnWithdraw behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnNotFoundForUnknownAccountOnWithdraw() throws Exception {
            when(accountService.withdraw(eq(99L), isA(BigDecimal.class)))
                    .thenThrow(new AccountNotFoundException("not found"));

            mockMvc.perform(post("/api/accounts/withdraw")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(amountRequest(99L, "100.00"))))
                    .andExpect(status().isNotFound());

            verify(accountService).withdraw(eq(99L), any(BigDecimal.class));
        }

    /*
     * Test Case: shouldReturnBadRequestForZeroWithdrawAmount
     * Description: Validates shouldReturnBadRequestForZeroWithdrawAmount behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnBadRequestForZeroWithdrawAmount() throws Exception {
            mockMvc.perform(post("/api/accounts/withdraw")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(amountRequest(1L, "0.00"))))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(accountService);
        }

    /*
     * Test Case: shouldReturnBadRequestForNegativeWithdrawAmount
     * Description: Validates shouldReturnBadRequestForNegativeWithdrawAmount behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnBadRequestForNegativeWithdrawAmount() throws Exception {
            mockMvc.perform(post("/api/accounts/withdraw")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(amountRequest(1L, "-1.00"))))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(accountService);
        }

    /*
     * Test Case: shouldReturnBadRequestForMissingWithdrawBody
     * Description: Validates shouldReturnBadRequestForMissingWithdrawBody behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnBadRequestForMissingWithdrawBody() throws Exception {
            mockMvc.perform(post("/api/accounts/withdraw")
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(accountService);
        }

    /*
     * Test Case: shouldReturnBadRequestForMalformedWithdrawJson
     * Description: Validates shouldReturnBadRequestForMalformedWithdrawJson behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnBadRequestForMalformedWithdrawJson() throws Exception {
            mockMvc.perform(post("/api/accounts/withdraw")
                            .contentType(APPLICATION_JSON)
                            .content("{\"amount\":"))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(accountService);
        }

    /*
     * Test Case: shouldReturnUnsupportedMediaTypeForWithdraw
     * Description: Validates shouldReturnUnsupportedMediaTypeForWithdraw behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnUnsupportedMediaTypeForWithdraw() throws Exception {
            mockMvc.perform(post("/api/accounts/withdraw")
                            .contentType(MediaType.TEXT_PLAIN)
                            .content("invalid"))
                    .andExpect(status().isUnsupportedMediaType());

            verifyNoInteractions(accountService);
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
        void shouldTransferSuccessfully() throws Exception {
            mockMvc.perform(post("/api/accounts/transfer")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(transferRequest(1L, 2L, "50.00"))))
                    .andExpect(status().isOk());

            verify(accountService).transfer(1L, 2L, new BigDecimal("50.00"));
        }

    /*
     * Test Case: shouldReturnNotFoundWhenSourceAccountMissing
     * Description: Validates shouldReturnNotFoundWhenSourceAccountMissing behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnNotFoundWhenSourceAccountMissing() throws Exception {
            org.mockito.Mockito.doThrow(new AccountNotFoundException("source not found"))
                    .when(accountService).transfer(1L, 2L, new BigDecimal("50.00"));

            mockMvc.perform(post("/api/accounts/transfer")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(transferRequest(1L, 2L, "50.00"))))
                    .andExpect(status().isNotFound());

            verify(accountService).transfer(1L, 2L, new BigDecimal("50.00"));
        }

    /*
     * Test Case: shouldReturnNotFoundWhenDestinationAccountMissing
     * Description: Validates shouldReturnNotFoundWhenDestinationAccountMissing behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnNotFoundWhenDestinationAccountMissing() throws Exception {
            org.mockito.Mockito.doThrow(new AccountNotFoundException("destination not found"))
                    .when(accountService).transfer(1L, 2L, new BigDecimal("75.00"));

            mockMvc.perform(post("/api/accounts/transfer")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(transferRequest(1L, 2L, "75.00"))))
                    .andExpect(status().isNotFound());

            verify(accountService).transfer(1L, 2L, new BigDecimal("75.00"));
        }

    /*
     * Test Case: shouldReturnBadRequestWhenTransferBetweenSameAccount
     * Description: Validates shouldReturnBadRequestWhenTransferBetweenSameAccount behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnBadRequestWhenTransferBetweenSameAccount() throws Exception {
            mockMvc.perform(post("/api/accounts/transfer")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(transferRequest(1L, 1L, "50.00"))))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(accountService);
        }

    /*
     * Test Case: shouldReturnBadRequestWhenTransferAmountZero
     * Description: Validates shouldReturnBadRequestWhenTransferAmountZero behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnBadRequestWhenTransferAmountZero() throws Exception {
            mockMvc.perform(post("/api/accounts/transfer")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(transferRequest(1L, 2L, "0.00"))))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(accountService);
        }

    /*
     * Test Case: shouldReturnBadRequestWhenTransferAmountNegative
     * Description: Validates shouldReturnBadRequestWhenTransferAmountNegative behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnBadRequestWhenTransferAmountNegative() throws Exception {
            mockMvc.perform(post("/api/accounts/transfer")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(transferRequest(1L, 2L, "-1.00"))))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(accountService);
        }

    /*
     * Test Case: shouldReturnBadRequestForInsufficientBalanceOnTransfer
     * Description: Validates shouldReturnBadRequestForInsufficientBalanceOnTransfer behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnBadRequestForInsufficientBalanceOnTransfer() throws Exception {
            org.mockito.Mockito.doThrow(new InsufficientBalanceException("insufficient"))
                    .when(accountService).transfer(1L, 2L, new BigDecimal("2000.00"));

            mockMvc.perform(post("/api/accounts/transfer")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(transferRequest(1L, 2L, "2000.00"))))
                    .andExpect(status().isBadRequest());

            verify(accountService).transfer(1L, 2L, new BigDecimal("2000.00"));
        }

    /*
     * Test Case: shouldReturnBadRequestForMalformedTransferJson
     * Description: Validates shouldReturnBadRequestForMalformedTransferJson behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnBadRequestForMalformedTransferJson() throws Exception {
            mockMvc.perform(post("/api/accounts/transfer")
                            .contentType(APPLICATION_JSON)
                            .content("{\"fromAccountId\":"))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(accountService);
        }

    /*
     * Test Case: shouldReturnUnsupportedMediaTypeForTransfer
     * Description: Validates shouldReturnUnsupportedMediaTypeForTransfer behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnUnsupportedMediaTypeForTransfer() throws Exception {
            mockMvc.perform(post("/api/accounts/transfer")
                            .contentType(MediaType.TEXT_PLAIN)
                            .content("invalid"))
                    .andExpect(status().isUnsupportedMediaType());

            verifyNoInteractions(accountService);
        }

    /*
     * Test Case: shouldReturnBadRequestForMissingTransferBody
     * Description: Validates shouldReturnBadRequestForMissingTransferBody behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnBadRequestForMissingTransferBody() throws Exception {
            mockMvc.perform(post("/api/accounts/transfer")
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(accountService);
        }
    }

    private AccountCreateRequest validCreateRequest() {
        AccountCreateRequest request = new AccountCreateRequest();
        request.setCustomerId(baseCreateRequest.getCustomerId());
        request.setAccountNumber(baseCreateRequest.getAccountNumber());
        request.setAccountType(baseCreateRequest.getAccountType());
        request.setOpeningBalance(baseCreateRequest.getOpeningBalance());
        return request;
    }

    private AccountUpdateRequest validUpdateRequest() {
        AccountUpdateRequest request = new AccountUpdateRequest();
        request.setAccountType(baseUpdateRequest.getAccountType());
        request.setAccountNumber(baseUpdateRequest.getAccountNumber());
        return request;
    }

    private Map<String, Object> amountRequest(Long accountId, String amount) {
        return Map.of("accountId", accountId, "amount", new BigDecimal(amount));
    }

    private Map<String, Object> transferRequest(Long fromAccountId, Long toAccountId, String amount) {
        return Map.of(
                "fromAccountId", fromAccountId,
                "toAccountId", toAccountId,
                "amount", new BigDecimal(amount));
    }

    private Account account(Long id, String accountNumber, String accountType, String balance) {
        Account account = new Account();
        account.setId(id != null ? id : baseAccount.getId());
        account.setAccountNumber(accountNumber != null ? accountNumber : baseAccount.getAccountNumber());
        account.setAccountType(accountType != null ? accountType : baseAccount.getAccountType());
        account.setBalance(new BigDecimal(balance != null ? balance : baseAccount.getBalance().toPlainString()));
        return account;
    }
}
