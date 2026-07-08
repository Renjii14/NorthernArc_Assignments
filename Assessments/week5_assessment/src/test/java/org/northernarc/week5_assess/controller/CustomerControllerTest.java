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

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.northernarc.week5_assess.dto.CustomerRegistrationRequest;
import org.northernarc.week5_assess.dto.CustomerUpdateRequest;
import org.northernarc.week5_assess.entity.Customer;
import org.northernarc.week5_assess.exception.CustomerNotFoundException;
import org.northernarc.week5_assess.exception.DuplicateEmailException;
import org.northernarc.week5_assess.exception.GlobalExceptionHandler;
import org.northernarc.week5_assess.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(CustomerController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
@DisplayName("Customer controller API behaviour")
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CustomerService customerService;

    private CustomerRegistrationRequest baseCreateRequest;
    private CustomerUpdateRequest baseUpdateRequest;
    private Customer baseCustomer;

    @BeforeEach
    void beforeEach(TestInfo testInfo) {
        baseCreateRequest = new CustomerRegistrationRequest();
        baseCreateRequest.setName("John Doe");
        baseCreateRequest.setEmail("john@example.com");
        baseCreateRequest.setPassword("pass@123");
        baseCreateRequest.setPhone("9876543210");

        baseUpdateRequest = new CustomerUpdateRequest();
        baseUpdateRequest.setName("John Updated");
        baseUpdateRequest.setEmail("john.updated@example.com");
        baseUpdateRequest.setPhone("9876543210");

        baseCustomer = new Customer();
        baseCustomer.setId(1L);
        baseCustomer.setName("John Doe");
        baseCustomer.setEmail("john@example.com");
        baseCustomer.setPhone("9876543210");

        System.out.println("Running test: " + testInfo.getDisplayName());
    }

    @Nested
    class CreateCustomer {

    /*
     * Test Case: shouldCreateCustomerSuccessfully
     * Description: Validates shouldCreateCustomerSuccessfully behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldCreateCustomerSuccessfully() throws Exception {
            Customer saved = customer(1L, "John Doe", "john@example.com", "9876543210");
            when(customerService.registerCustomer(isA(CustomerRegistrationRequest.class))).thenReturn(saved);

            mockMvc.perform(post("/api/customers")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validCreateRequest())))
                    .andExpect(status().isCreated())
                    .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.name").value("John Doe"))
                    .andExpect(jsonPath("$.email").value("john@example.com"))
                    .andExpect(jsonPath("$.phone").value("9876543210"));

            verify(customerService).registerCustomer(any(CustomerRegistrationRequest.class));
        }

    /*
     * Test Case: shouldReturnConflictWhenDuplicateEmail
     * Description: Validates shouldReturnConflictWhenDuplicateEmail behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnConflictWhenDuplicateEmail() throws Exception {
            when(customerService.registerCustomer(isA(CustomerRegistrationRequest.class)))
                    .thenThrow(new DuplicateEmailException("duplicate email"));

            mockMvc.perform(post("/api/customers")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validCreateRequest())))
                    .andExpect(status().isConflict());

            verify(customerService).registerCustomer(any(CustomerRegistrationRequest.class));
        }

    /*
     * Test Case: shouldReturnBadRequestForInvalidEmail
     * Description: Validates shouldReturnBadRequestForInvalidEmail behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnBadRequestForInvalidEmail() throws Exception {
            CustomerRegistrationRequest request = validCreateRequest();
            request.setEmail("invalid-email");

            mockMvc.perform(post("/api/customers")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(customerService);
        }

    /*
     * Test Case: shouldReturnBadRequestForBlankName
     * Description: Validates shouldReturnBadRequestForBlankName behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnBadRequestForBlankName() throws Exception {
            CustomerRegistrationRequest request = validCreateRequest();
            request.setName(" ");

            mockMvc.perform(post("/api/customers")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(customerService);
        }

    /*
     * Test Case: shouldReturnBadRequestForInvalidPhone
     * Description: Validates shouldReturnBadRequestForInvalidPhone behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnBadRequestForInvalidPhone() throws Exception {
            CustomerRegistrationRequest request = validCreateRequest();
            request.setPhone("98765AB210");

            mockMvc.perform(post("/api/customers")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(customerService);
        }

    /*
     * Test Case: shouldReturnBadRequestForMissingBody
     * Description: Validates shouldReturnBadRequestForMissingBody behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnBadRequestForMissingBody() throws Exception {
            mockMvc.perform(post("/api/customers")
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(customerService);
        }

    /*
     * Test Case: shouldReturnBadRequestForMalformedJson
     * Description: Validates shouldReturnBadRequestForMalformedJson behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnBadRequestForMalformedJson() throws Exception {
            mockMvc.perform(post("/api/customers")
                            .contentType(APPLICATION_JSON)
                            .content("{\"name\":\"John\""))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(customerService);
        }

    /*
     * Test Case: shouldReturnUnsupportedMediaTypeForNonJsonRequest
     * Description: Validates shouldReturnUnsupportedMediaTypeForNonJsonRequest behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnUnsupportedMediaTypeForNonJsonRequest() throws Exception {
            mockMvc.perform(post("/api/customers")
                            .contentType(MediaType.TEXT_PLAIN)
                            .content("invalid"))
                    .andExpect(status().isUnsupportedMediaType());

            verifyNoInteractions(customerService);
        }
    }

    @Nested
    class GetCustomers {

    /*
     * Test Case: shouldReturnCustomerListSuccessfully
     * Description: Validates shouldReturnCustomerListSuccessfully behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnCustomerListSuccessfully() throws Exception {
            when(customerService.getAllCustomers())
                    .thenReturn(List.of(customer(1L, "John Doe", "john@example.com", "9876543210")));

            mockMvc.perform(get("/api/customers"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                    .andExpect(jsonPath("$[0].id").value(1L))
                    .andExpect(jsonPath("$[0].name").value("John Doe"))
                    .andExpect(jsonPath("$[0].email").value("john@example.com"));

            verify(customerService).getAllCustomers();
        }

    /*
     * Test Case: shouldReturnEmptyArrayWhenNoCustomers
     * Description: Validates shouldReturnEmptyArrayWhenNoCustomers behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnEmptyArrayWhenNoCustomers() throws Exception {
            when(customerService.getAllCustomers()).thenReturn(List.of());

            mockMvc.perform(get("/api/customers"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                    .andExpect(content().json("[]"));

            verify(customerService).getAllCustomers();
        }
    }

    @Nested
    class GetCustomerById {

    /*
     * Test Case: shouldReturnCustomerWhenIdExists
     * Description: Validates shouldReturnCustomerWhenIdExists behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnCustomerWhenIdExists() throws Exception {
            when(customerService.getCustomerById(1L)).thenReturn(customer(1L, "John Doe", "john@example.com", "9876543210"));

            mockMvc.perform(get("/api/customers/{id}", 1L))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.email").value("john@example.com"));

            verify(customerService).getCustomerById(1L);
        }

    /*
     * Test Case: shouldReturnNotFoundWhenCustomerDoesNotExist
     * Description: Validates shouldReturnNotFoundWhenCustomerDoesNotExist behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnNotFoundWhenCustomerDoesNotExist() throws Exception {
            when(customerService.getCustomerById(99L)).thenThrow(new CustomerNotFoundException("not found"));

            mockMvc.perform(get("/api/customers/{id}", 99L))
                    .andExpect(status().isNotFound());

            verify(customerService).getCustomerById(99L);
        }

    /*
     * Test Case: shouldReturnBadRequestWhenPathVariableIsInvalid
     * Description: Validates shouldReturnBadRequestWhenPathVariableIsInvalid behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnBadRequestWhenPathVariableIsInvalid() throws Exception {
            mockMvc.perform(get("/api/customers/{id}", "abc"))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(customerService);
        }
    }

    @Nested
    class UpdateCustomer {

    /*
     * Test Case: shouldUpdateCustomerSuccessfully
     * Description: Validates shouldUpdateCustomerSuccessfully behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldUpdateCustomerSuccessfully() throws Exception {
            Customer updated = customer(1L, "John Updated", "john.updated@example.com", "9876543210");
            when(customerService.updateCustomer(eq(1L), isA(CustomerUpdateRequest.class))).thenReturn(updated);

            mockMvc.perform(put("/api/customers/{id}", 1L)
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validUpdateRequest())))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                    .andExpect(jsonPath("$.name").value("John Updated"))
                    .andExpect(jsonPath("$.email").value("john.updated@example.com"));

            verify(customerService).updateCustomer(eq(1L), any(CustomerUpdateRequest.class));
        }

    /*
     * Test Case: shouldReturnNotFoundWhenUpdatingUnknownCustomer
     * Description: Validates shouldReturnNotFoundWhenUpdatingUnknownCustomer behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnNotFoundWhenUpdatingUnknownCustomer() throws Exception {
            when(customerService.updateCustomer(eq(99L), isA(CustomerUpdateRequest.class)))
                    .thenThrow(new CustomerNotFoundException("not found"));

            mockMvc.perform(put("/api/customers/{id}", 99L)
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validUpdateRequest())))
                    .andExpect(status().isNotFound());

            verify(customerService).updateCustomer(eq(99L), any(CustomerUpdateRequest.class));
        }

    /*
     * Test Case: shouldReturnBadRequestForInvalidRequest
     * Description: Validates shouldReturnBadRequestForInvalidRequest behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnBadRequestForInvalidRequest() throws Exception {
            CustomerUpdateRequest request = validUpdateRequest();
            request.setEmail("bad-email");

            mockMvc.perform(put("/api/customers/{id}", 1L)
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(customerService);
        }

    /*
     * Test Case: shouldReturnConflictWhenUpdateEmailConflicts
     * Description: Validates shouldReturnConflictWhenUpdateEmailConflicts behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnConflictWhenUpdateEmailConflicts() throws Exception {
            when(customerService.updateCustomer(eq(1L), isA(CustomerUpdateRequest.class)))
                    .thenThrow(new DuplicateEmailException("duplicate"));

            mockMvc.perform(put("/api/customers/{id}", 1L)
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validUpdateRequest())))
                    .andExpect(status().isConflict());

            verify(customerService).updateCustomer(eq(1L), any(CustomerUpdateRequest.class));
        }

    /*
     * Test Case: shouldReturnBadRequestWhenUpdatePathVariableIsInvalid
     * Description: Validates shouldReturnBadRequestWhenUpdatePathVariableIsInvalid behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnBadRequestWhenUpdatePathVariableIsInvalid() throws Exception {
            mockMvc.perform(put("/api/customers/{id}", "bad")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validUpdateRequest())))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(customerService);
        }

    /*
     * Test Case: shouldReturnBadRequestForMalformedJsonOnUpdate
     * Description: Validates shouldReturnBadRequestForMalformedJsonOnUpdate behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnBadRequestForMalformedJsonOnUpdate() throws Exception {
            mockMvc.perform(put("/api/customers/{id}", 1L)
                            .contentType(APPLICATION_JSON)
                            .content("{\"name\":"))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(customerService);
        }

    /*
     * Test Case: shouldReturnUnsupportedMediaTypeForUpdateRequest
     * Description: Validates shouldReturnUnsupportedMediaTypeForUpdateRequest behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnUnsupportedMediaTypeForUpdateRequest() throws Exception {
            mockMvc.perform(put("/api/customers/{id}", 1L)
                            .contentType(MediaType.TEXT_PLAIN)
                            .content("invalid"))
                    .andExpect(status().isUnsupportedMediaType());

            verifyNoInteractions(customerService);
        }
    }

    @Nested
    class DeleteCustomer {

    /*
     * Test Case: shouldDeleteCustomerSuccessfully
     * Description: Validates shouldDeleteCustomerSuccessfully behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldDeleteCustomerSuccessfully() throws Exception {
            mockMvc.perform(delete("/api/customers/{id}", 1L))
                    .andExpect(status().isNoContent());

            verify(customerService).deleteCustomer(1L);
        }

    /*
     * Test Case: shouldReturnNotFoundWhenDeletingUnknownCustomer
     * Description: Validates shouldReturnNotFoundWhenDeletingUnknownCustomer behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnNotFoundWhenDeletingUnknownCustomer() throws Exception {
            org.mockito.Mockito.doThrow(new CustomerNotFoundException("not found"))
                    .when(customerService).deleteCustomer(99L);

            mockMvc.perform(delete("/api/customers/{id}", 99L))
                    .andExpect(status().isNotFound());

            verify(customerService).deleteCustomer(99L);
        }

    /*
     * Test Case: shouldReturnBadRequestWhenDeletePathVariableIsInvalid
     * Description: Validates shouldReturnBadRequestWhenDeletePathVariableIsInvalid behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnBadRequestWhenDeletePathVariableIsInvalid() throws Exception {
            mockMvc.perform(delete("/api/customers/{id}", "x"))
                    .andExpect(status().isBadRequest());

            verify(customerService, never()).deleteCustomer(anyLong());
        }
    }

    private CustomerRegistrationRequest validCreateRequest() {
        CustomerRegistrationRequest request = new CustomerRegistrationRequest();
        request.setName(baseCreateRequest.getName());
        request.setEmail(baseCreateRequest.getEmail());
        request.setPassword(baseCreateRequest.getPassword());
        request.setPhone(baseCreateRequest.getPhone());
        return request;
    }

    private CustomerUpdateRequest validUpdateRequest() {
        CustomerUpdateRequest request = new CustomerUpdateRequest();
        request.setName(baseUpdateRequest.getName());
        request.setEmail(baseUpdateRequest.getEmail());
        request.setPhone(baseUpdateRequest.getPhone());
        return request;
    }

    private Customer customer(Long id, String name, String email, String phone) {
        Customer customer = new Customer();
        customer.setId(id != null ? id : baseCustomer.getId());
        customer.setName(name != null ? name : baseCustomer.getName());
        customer.setEmail(email != null ? email : baseCustomer.getEmail());
        customer.setPhone(phone != null ? phone : baseCustomer.getPhone());
        return customer;
    }
}
