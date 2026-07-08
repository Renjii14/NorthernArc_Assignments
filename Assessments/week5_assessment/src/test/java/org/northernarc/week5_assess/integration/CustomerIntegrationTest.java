package org.northernarc.week5_assess.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;
import java.util.HashMap;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.northernarc.week5_assess.entity.Customer;
import org.northernarc.week5_assess.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.test.context.support.WithMockUser;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = Replace.ANY)
@Transactional
@DisplayName("Customer integration flow")
class CustomerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void beforeEach(TestInfo testInfo) {
        assertThat(mockMvc).isNotNull();
        assertThat(objectMapper).isNotNull();
        assertThat(customerRepository).isNotNull();
        System.out.println("Running test: " + testInfo.getDisplayName());
    }

    /*
     * Test Case: shouldRegisterCustomerSuccessfullyAndPersistEncryptedPassword
     * Description: Validates shouldRegisterCustomerSuccessfullyAndPersistEncryptedPassword behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
    @Test
    void shouldRegisterCustomerSuccessfullyAndPersistEncryptedPassword() throws Exception {
        long countBefore = customerRepository.count();

        MvcResult result = mockMvc.perform(post("/api/auth/register")
                        .contentType(APPLICATION_JSON)
                        .content(toJson(registerRequest("John Doe", "john@example.com", "pass@123", "9876543210"))))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andReturn();

        JsonNode json = objectMapper.readTree(result.getResponse().getContentAsString());
        assertThat(json.get("id").asLong()).isPositive();
        assertThat(customerRepository.count()).isEqualTo(countBefore + 1);

        Customer persisted = customerRepository.findByEmail("john@example.com").orElseThrow();
        assertThat(persisted.getPassword()).isNotBlank();
        assertThat(persisted.getPassword()).isNotEqualTo("pass@123");
    }

    /*
     * Test Case: shouldReturnConflictWhenRegisteringDuplicateEmail
     * Description: Validates shouldReturnConflictWhenRegisteringDuplicateEmail behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
    @Test
    void shouldReturnConflictWhenRegisteringDuplicateEmail() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                        .contentType(APPLICATION_JSON)
                        .content(toJson(registerRequest("John Doe", "john@example.com", "pass@123", "9876543210"))))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/auth/register")
                        .contentType(APPLICATION_JSON)
                        .content(toJson(registerRequest("Jane Roe", "john@example.com", "pass@123", "9123456789"))))
                .andExpect(status().isConflict())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(409));
    }

    /*
     * Test Case: shouldReturnBadRequestForInvalidRegistrationPayloads
     * Description: Validates shouldReturnBadRequestForInvalidRegistrationPayloads behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
    @Test
    void shouldReturnBadRequestForInvalidRegistrationPayloads() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                        .contentType(APPLICATION_JSON)
                        .content(toJson(registerRequest("John", "bad-email", "pass@123", "9876543210"))))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/auth/register")
                        .contentType(APPLICATION_JSON)
                        .content(toJson(registerRequest(null, "john2@example.com", "pass@123", "9876543210"))))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/auth/register")
                        .contentType(APPLICATION_JSON)
                        .content(toJson(registerRequest("John", "john3@example.com", null, "9876543210"))))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/auth/register")
                        .contentType(APPLICATION_JSON)
                        .content(toJson(registerRequest("John", "john4@example.com", "pass@123", "98AB543210"))))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/auth/register")
                        .contentType(APPLICATION_JSON)
                        .content(toJson(registerRequest(" ", " ", " ", " "))))
                .andExpect(status().isBadRequest());
    }

    /*
     * Test Case: shouldExecuteCompleteCustomerCrudFlowAndReflectDatabaseState
     * Description: Validates shouldExecuteCompleteCustomerCrudFlowAndReflectDatabaseState behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
    @Test
    void shouldExecuteCompleteCustomerCrudFlowAndReflectDatabaseState() throws Exception {
        String token = registerAndLogin("admin@example.com", "pass@123", "9876543210");

        MvcResult createdResult = mockMvc.perform(post("/api/customers")
                        .header("Authorization", bearer(token))
                        .contentType(APPLICATION_JSON)
                        .content(toJson(customerCreateRequest("Jane Roe", "jane@example.com", "pass@123", "9123456789"))))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Jane Roe"))
                .andReturn();

        long customerId = objectMapper.readTree(createdResult.getResponse().getContentAsString()).get("id").asLong();
        assertThat(customerRepository.findById(customerId)).isPresent();

        mockMvc.perform(get("/api/customers/{id}", customerId).header("Authorization", bearer(token)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value("jane@example.com"));

        MvcResult listResult = mockMvc.perform(get("/api/customers").header("Authorization", bearer(token)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        assertThat(listResult.getResponse().getContentAsString()).contains("\"id\":" + customerId);

        mockMvc.perform(put("/api/customers/{id}", customerId)
                        .header("Authorization", bearer(token))
                        .contentType(APPLICATION_JSON)
                        .content(toJson(customerUpdateRequest("Jane Updated", "jane.updated@example.com", "9988776655"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Jane Updated"))
                .andExpect(jsonPath("$.email").value("jane.updated@example.com"));

        Customer updated = customerRepository.findById(customerId).orElseThrow();
        assertThat(updated.getName()).isEqualTo("Jane Updated");
        assertThat(updated.getEmail()).isEqualTo("jane.updated@example.com");

        mockMvc.perform(delete("/api/customers/{id}", customerId).header("Authorization", bearer(token)))
                .andExpect(status().isNoContent());

        assertThat(customerRepository.findById(customerId)).isEmpty();

        mockMvc.perform(get("/api/customers/{id}", customerId).header("Authorization", bearer(token)))
                .andExpect(status().isNotFound());
    }

    /*
     * Test Case: shouldHandleCustomerEdgeCasesForStatusAndPayloadValidation
     * Description: Validates shouldHandleCustomerEdgeCasesForStatusAndPayloadValidation behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
    @Test
    void shouldHandleCustomerEdgeCasesForStatusAndPayloadValidation() throws Exception {
        String token = registerAndLogin("root@example.com", "pass@123", "9876543210");

        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/api/customers/{id}", "bad-id")
                        .header("Authorization", bearer(token)))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/api/customers/{id}", 999999L)
                        .header("Authorization", bearer(token)))
                .andExpect(status().isNotFound());

        mockMvc.perform(post("/api/customers")
                        .header("Authorization", bearer(token))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/customers")
                        .header("Authorization", bearer(token))
                        .contentType(APPLICATION_JSON)
                        .content("{\"name\":"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/customers")
                        .header("Authorization", bearer(token))
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("invalid"))
                .andExpect(status().isUnsupportedMediaType());
    }

    /*
     * Test Case: shouldReturnCustomersListForMultipleRecordsAndEmptyArrayWhenNoCustomers
     * Description: Validates shouldReturnCustomersListForMultipleRecordsAndEmptyArrayWhenNoCustomers behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
    @Test
    void shouldReturnCustomersListForMultipleRecordsAndEmptyArrayWhenNoCustomers() throws Exception {
        String token = registerAndLogin("ops@example.com", "pass@123", "9876543210");

        mockMvc.perform(get("/api/customers").header("Authorization", bearer(token)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON));

        mockMvc.perform(post("/api/auth/register")
                        .contentType(APPLICATION_JSON)
                        .content(toJson(registerRequest("John Doe", "john5@example.com", "pass@123", "9876543211"))))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/auth/register")
                        .contentType(APPLICATION_JSON)
                        .content(toJson(registerRequest("Jane Roe", "jane5@example.com", "pass@123", "9876543212"))))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/customers").header("Authorization", bearer(token)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(customerRepository.findAll().size()));
    }

    /*
     * Test Case: shouldReturnEmptyCustomerListWhenDatabaseHasNoCustomers
     * Description: Validates shouldReturnEmptyCustomerListWhenDatabaseHasNoCustomers behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
    @Test
    @WithMockUser(username = "tester", roles = "USER")
    void shouldReturnEmptyCustomerListWhenDatabaseHasNoCustomers() throws Exception {
        customerRepository.deleteAll();

        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(content().json("[]"));
    }

    private String registerAndLogin(String email, String password, String phone) throws Exception {
        mockMvc.perform(post("/api/auth/register")
                        .contentType(APPLICATION_JSON)
                        .content(toJson(registerRequest("Auth User", email, password, phone))))
                .andExpect(status().isCreated());

        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(toJson(loginRequest(email, password))))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andReturn();

        return objectMapper.readTree(loginResult.getResponse().getContentAsString()).get("token").asText();
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

    private Map<String, Object> customerCreateRequest(String name, String email, String password, String phone) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("email", email);
        map.put("password", password);
        map.put("phone", phone);
        return map;
    }

    private Map<String, Object> customerUpdateRequest(String name, String email, String phone) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("email", email);
        map.put("phone", phone);
        return map;
    }
}
