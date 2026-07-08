package org.northernarc.week5_assess.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.northernarc.week5_assess.entity.Account;
import org.northernarc.week5_assess.entity.Customer;
import org.northernarc.week5_assess.entity.Transaction;
import org.northernarc.week5_assess.repository.AccountRepository;
import org.northernarc.week5_assess.repository.CustomerRepository;
import org.northernarc.week5_assess.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = Replace.ANY)
@Transactional
@DisplayName("Transfer and banking integration flow")
class TransferIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @BeforeAll
    static void beforeAll() {
        System.out.println("TransferIntegrationTest - suite started");
    }

    @BeforeEach
    void beforeEach(TestInfo testInfo) {
        assertThat(mockMvc).isNotNull();
        assertThat(customerRepository).isNotNull();
        assertThat(accountRepository).isNotNull();
        assertThat(transactionRepository).isNotNull();
        System.out.println("Running test: " + testInfo.getDisplayName());
    }

    @AfterEach
    void afterEach() {
        System.out.println("TransferIntegrationTest - test finished (customers=" + customerRepository.count()
                + ", accounts=" + accountRepository.count()
                + ", transactions=" + transactionRepository.count() + ")");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("TransferIntegrationTest - suite finished");
    }

    /*
     * Test Case: shouldExecuteDepositWithdrawalAndTransferFlowWithDatabaseConsistency
     * Description: Validates shouldExecuteDepositWithdrawalAndTransferFlowWithDatabaseConsistency behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
    @Test
    void shouldExecuteDepositWithdrawalAndTransferFlowWithDatabaseConsistency() throws Exception {
        String token = registerAndLogin("owner1@example.com", "pass@123", "9876543210");
        long customerOneId = customerRepository.findByEmail("owner1@example.com").map(Customer::getId).orElseThrow();

        mockMvc.perform(post("/api/auth/register")
                        .contentType(APPLICATION_JSON)
                        .content(toJson(registerRequest("Owner Two", "owner2@example.com", "pass@123", "9876543211"))))
                .andExpect(status().isCreated());
        long customerTwoId = customerRepository.findByEmail("owner2@example.com").map(Customer::getId).orElseThrow();

        long accountOneId = createAccount(token, customerOneId, "SB100001", "SAVINGS", "1000.00");
        long accountTwoId = createAccount(token, customerTwoId, "CA100001", "CURRENT", "500.00");

        long txCountBefore = transactionRepository.count();

        mockMvc.perform(post("/api/accounts/deposit")
                        .header("Authorization", bearer(token))
                        .contentType(APPLICATION_JSON)
                        .content(toJson(amountRequest(accountOneId, "200.00"))))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.balance").value(1200.00));

        assertThat(accountRepository.findById(accountOneId).orElseThrow().getBalance())
                .isEqualByComparingTo("1200.00");

        mockMvc.perform(post("/api/accounts/withdraw")
                        .header("Authorization", bearer(token))
                        .contentType(APPLICATION_JSON)
                        .content(toJson(amountRequest(accountOneId, "100.00"))))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.balance").value(1100.00));

        assertThat(accountRepository.findById(accountOneId).orElseThrow().getBalance())
                .isEqualByComparingTo("1100.00");

        mockMvc.perform(post("/api/accounts/transfer")
                        .header("Authorization", bearer(token))
                        .contentType(APPLICATION_JSON)
                        .content(toJson(transferRequest(accountOneId, accountTwoId, "300.00"))))
                .andExpect(status().isOk());

        Account source = accountRepository.findById(accountOneId).orElseThrow();
        Account destination = accountRepository.findById(accountTwoId).orElseThrow();
        assertThat(source.getBalance()).isEqualByComparingTo("800.00");
        assertThat(destination.getBalance()).isEqualByComparingTo("800.00");

        List<Transaction> sourceTransactions = transactionRepository.findByAccountId(accountOneId);
        List<Transaction> destinationTransactions = transactionRepository.findByAccountId(accountTwoId);
        assertThat(sourceTransactions).isNotEmpty();
        assertThat(destinationTransactions).isNotEmpty();
        assertThat(transactionRepository.count()).isGreaterThanOrEqualTo(txCountBefore + 4);
    }

    /*
     * Test Case: shouldReturnExpectedStatusForDepositEdgeCases
     * Description: Validates shouldReturnExpectedStatusForDepositEdgeCases behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
    @Test
    void shouldReturnExpectedStatusForDepositEdgeCases() throws Exception {
        String token = registerAndLogin("depuser@example.com", "pass@123", "9876543210");
        long customerId = customerRepository.findByEmail("depuser@example.com").map(Customer::getId).orElseThrow();
        long accountId = createAccount(token, customerId, "SB200001", "SAVINGS", "100.00");

        mockMvc.perform(post("/api/accounts/deposit")
                        .header("Authorization", bearer(token))
                        .contentType(APPLICATION_JSON)
                        .content(toJson(amountRequest(accountId, "0.00"))))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/accounts/deposit")
                        .header("Authorization", bearer(token))
                        .contentType(APPLICATION_JSON)
                        .content(toJson(amountRequest(accountId, "-1.00"))))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/accounts/deposit")
                        .header("Authorization", bearer(token))
                        .contentType(APPLICATION_JSON)
                        .content(toJson(amountRequest(999999L, "10.00"))))
                .andExpect(status().isNotFound());

        mockMvc.perform(post("/api/accounts/deposit")
                        .header("Authorization", bearer(token))
                        .contentType(APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    /*
     * Test Case: shouldReturnExpectedStatusForWithdrawalEdgeCases
     * Description: Validates shouldReturnExpectedStatusForWithdrawalEdgeCases behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
    @Test
    void shouldReturnExpectedStatusForWithdrawalEdgeCases() throws Exception {
        String token = registerAndLogin("wduser@example.com", "pass@123", "9876543210");
        long customerId = customerRepository.findByEmail("wduser@example.com").map(Customer::getId).orElseThrow();
        long accountId = createAccount(token, customerId, "SB300001", "SAVINGS", "100.00");

        mockMvc.perform(post("/api/accounts/withdraw")
                        .header("Authorization", bearer(token))
                        .contentType(APPLICATION_JSON)
                        .content(toJson(amountRequest(accountId, "100.00"))))
                .andExpect(status().isOk());
        assertThat(accountRepository.findById(accountId).orElseThrow().getBalance()).isEqualByComparingTo("0.00");

        mockMvc.perform(post("/api/accounts/withdraw")
                        .header("Authorization", bearer(token))
                        .contentType(APPLICATION_JSON)
                        .content(toJson(amountRequest(accountId, "1.00"))))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/accounts/withdraw")
                        .header("Authorization", bearer(token))
                        .contentType(APPLICATION_JSON)
                        .content(toJson(amountRequest(accountId, "0.00"))))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/accounts/withdraw")
                        .header("Authorization", bearer(token))
                        .contentType(APPLICATION_JSON)
                        .content(toJson(amountRequest(accountId, "-1.00"))))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/accounts/withdraw")
                        .header("Authorization", bearer(token))
                        .contentType(APPLICATION_JSON)
                        .content(toJson(amountRequest(123456L, "10.00"))))
                .andExpect(status().isNotFound());
    }

    /*
     * Test Case: shouldHandleTransferEdgeCasesAndPreserveAtomicityOnFailure
     * Description: Validates shouldHandleTransferEdgeCasesAndPreserveAtomicityOnFailure behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
    @Test
    void shouldHandleTransferEdgeCasesAndPreserveAtomicityOnFailure() throws Exception {
        String token = registerAndLogin("src@example.com", "pass@123", "9876543220");
        mockMvc.perform(post("/api/auth/register")
                        .contentType(APPLICATION_JSON)
                        .content(toJson(registerRequest("Dest", "dest@example.com", "pass@123", "9876543221"))))
                .andExpect(status().isCreated());

        long sourceCustomerId = customerRepository.findByEmail("src@example.com").map(Customer::getId).orElseThrow();
        long destCustomerId = customerRepository.findByEmail("dest@example.com").map(Customer::getId).orElseThrow();
        long sourceId = createAccount(token, sourceCustomerId, "SB400001", "SAVINGS", "500.00");
        long destId = createAccount(token, destCustomerId, "CA400001", "CURRENT", "200.00");

        BigDecimal sourceBefore = accountRepository.findById(sourceId).orElseThrow().getBalance();
        BigDecimal destBefore = accountRepository.findById(destId).orElseThrow().getBalance();
        long txBefore = transactionRepository.count();

        mockMvc.perform(post("/api/accounts/transfer")
                        .header("Authorization", bearer(token))
                        .contentType(APPLICATION_JSON)
                        .content(toJson(transferRequest(99999L, destId, "50.00"))))
                .andExpect(status().isNotFound());

        mockMvc.perform(post("/api/accounts/transfer")
                        .header("Authorization", bearer(token))
                        .contentType(APPLICATION_JSON)
                        .content(toJson(transferRequest(sourceId, 99999L, "50.00"))))
                .andExpect(status().isNotFound());

        mockMvc.perform(post("/api/accounts/transfer")
                        .header("Authorization", bearer(token))
                        .contentType(APPLICATION_JSON)
                        .content(toJson(transferRequest(sourceId, sourceId, "50.00"))))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/accounts/transfer")
                        .header("Authorization", bearer(token))
                        .contentType(APPLICATION_JSON)
                        .content(toJson(transferRequest(sourceId, destId, "0.00"))))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/accounts/transfer")
                        .header("Authorization", bearer(token))
                        .contentType(APPLICATION_JSON)
                        .content(toJson(transferRequest(sourceId, destId, "-1.00"))))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/accounts/transfer")
                        .header("Authorization", bearer(token))
                        .contentType(APPLICATION_JSON)
                        .content(toJson(transferRequest(sourceId, destId, "9999.00"))))
                .andExpect(status().isBadRequest());

        assertThat(accountRepository.findById(sourceId).orElseThrow().getBalance()).isEqualByComparingTo(sourceBefore);
        assertThat(accountRepository.findById(destId).orElseThrow().getBalance()).isEqualByComparingTo(destBefore);
        assertThat(transactionRepository.count()).isEqualTo(txBefore);
    }

    /*
     * Test Case: shouldReturnCorrectTransactionHistoryAndStatuses
     * Description: Validates shouldReturnCorrectTransactionHistoryAndStatuses behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
    @Test
    void shouldReturnCorrectTransactionHistoryAndStatuses() throws Exception {
        String token = registerAndLogin("history1@example.com", "pass@123", "9876543230");
        mockMvc.perform(post("/api/auth/register")
                        .contentType(APPLICATION_JSON)
                        .content(toJson(registerRequest("History Two", "history2@example.com", "pass@123", "9876543231"))))
                .andExpect(status().isCreated());

        long customerOneId = customerRepository.findByEmail("history1@example.com").map(Customer::getId).orElseThrow();
        long customerTwoId = customerRepository.findByEmail("history2@example.com").map(Customer::getId).orElseThrow();
        long sourceId = createAccount(token, customerOneId, "SB500001", "SAVINGS", "800.00");
        long destId = createAccount(token, customerTwoId, "CA500001", "CURRENT", "100.00");

        mockMvc.perform(post("/api/accounts/transfer")
                        .header("Authorization", bearer(token))
                        .contentType(APPLICATION_JSON)
                        .content(toJson(transferRequest(sourceId, destId, "150.00"))))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/accounts/{id}/transactions", sourceId).header("Authorization", bearer(token)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(transactionRepository.findByAccountId(sourceId).size()));

        mockMvc.perform(get("/api/accounts/{id}/transactions", 999999L).header("Authorization", bearer(token)))
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/api/accounts/{id}/transactions", sourceId))
                .andExpect(status().isUnauthorized());
    }

    /*
     * Test Case: shouldReturnEmptyTransactionHistoryWhenAccountHasNoTransactions
     * Description: Validates shouldReturnEmptyTransactionHistoryWhenAccountHasNoTransactions behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
    @Test
    void shouldReturnEmptyTransactionHistoryWhenAccountHasNoTransactions() throws Exception {
        String token = registerAndLogin("emptytx@example.com", "pass@123", "9876543250");
        long customerId = customerRepository.findByEmail("emptytx@example.com").map(Customer::getId).orElseThrow();
        long accountId = createAccount(token, customerId, "SB700001", "SAVINGS", "250.00");

        mockMvc.perform(get("/api/accounts/{id}/transactions", accountId).header("Authorization", bearer(token)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(content().json("[]"));
    }

    /*
     * Test Case: shouldReturnBadRequestForMalformedTransferRequest
     * Description: Validates shouldReturnBadRequestForMalformedTransferRequest behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
    @Test
    void shouldReturnBadRequestForMalformedTransferRequest() throws Exception {
        String token = registerAndLogin("badtransfer@example.com", "pass@123", "9876543260");

        mockMvc.perform(post("/api/accounts/transfer")
                        .header("Authorization", bearer(token))
                        .contentType(APPLICATION_JSON)
                        .content("{\"fromAccountId\":"))
                .andExpect(status().isBadRequest());
    }

    /*
     * Test Case: shouldReturnConflictForDuplicateAccountNumberAndUnsupportedMediaType
     * Description: Validates shouldReturnConflictForDuplicateAccountNumberAndUnsupportedMediaType behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
    @Test
    void shouldReturnConflictForDuplicateAccountNumberAndUnsupportedMediaType() throws Exception {
        String token = registerAndLogin("dupacc@example.com", "pass@123", "9876543240");
        long customerId = customerRepository.findByEmail("dupacc@example.com").map(Customer::getId).orElseThrow();

        createAccount(token, customerId, "SB600001", "SAVINGS", "100.00");

        mockMvc.perform(post("/api/accounts")
                        .header("Authorization", bearer(token))
                        .contentType(APPLICATION_JSON)
                        .content(toJson(accountCreateRequest(customerId, "SB600001", "SAVINGS", "200.00"))))
                .andExpect(status().isConflict());

        mockMvc.perform(post("/api/accounts")
                        .header("Authorization", bearer(token))
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("invalid"))
                .andExpect(status().isUnsupportedMediaType());
    }

    private long createAccount(String token, long customerId, String accountNumber, String type, String balance)
            throws Exception {
        MvcResult result = mockMvc.perform(post("/api/accounts")
                        .header("Authorization", bearer(token))
                        .contentType(APPLICATION_JSON)
                        .content(toJson(accountCreateRequest(customerId, accountNumber, type, balance))))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.accountNumber").value(accountNumber))
                .andReturn();

        JsonNode json = objectMapper.readTree(result.getResponse().getContentAsString());
        return json.get("id").asLong();
    }

    private String registerAndLogin(String email, String password, String phone) throws Exception {
        mockMvc.perform(post("/api/auth/register")
                        .contentType(APPLICATION_JSON)
                        .content(toJson(registerRequest("Token User", email, password, phone))))
                .andExpect(status().isCreated());

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(toJson(loginRequest(email, password))))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andReturn();

        return objectMapper.readTree(result.getResponse().getContentAsString()).get("token").asText();
    }

    private String toJson(Object body) throws Exception {
        return objectMapper.writeValueAsString(body);
    }

    private String bearer(String token) {
        return "Bearer " + token;
    }

    private Map<String, Object> registerRequest(String name, String email, String password, String phone) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("email", email);
        map.put("password", password);
        map.put("phone", phone);
        return map;
    }

    private Map<String, Object> loginRequest(String email, String password) {
        Map<String, Object> map = new HashMap<>();
        map.put("email", email);
        map.put("password", password);
        return map;
    }

    private Map<String, Object> accountCreateRequest(long customerId, String accountNumber, String type, String balance) {
        Map<String, Object> map = new HashMap<>();
        map.put("customerId", customerId);
        map.put("accountNumber", accountNumber);
        map.put("accountType", type);
        map.put("openingBalance", new BigDecimal(balance));
        return map;
    }

    private Map<String, Object> amountRequest(long accountId, String amount) {
        Map<String, Object> map = new HashMap<>();
        map.put("accountId", accountId);
        map.put("amount", new BigDecimal(amount));
        return map;
    }

    private Map<String, Object> transferRequest(long fromAccountId, long toAccountId, String amount) {
        Map<String, Object> map = new HashMap<>();
        map.put("fromAccountId", fromAccountId);
        map.put("toAccountId", toAccountId);
        map.put("amount", new BigDecimal(amount));
        return map;
    }
}
