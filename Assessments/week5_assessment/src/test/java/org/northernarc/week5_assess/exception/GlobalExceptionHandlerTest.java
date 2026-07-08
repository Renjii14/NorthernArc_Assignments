package org.northernarc.week5_assess.exception;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@WebMvcTest(controllers = GlobalExceptionHandlerTest.ExceptionThrowingController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
@DisplayName("Global exception handler mappings")
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void beforeEach(TestInfo testInfo) {
        System.out.println("Running test: " + testInfo.getDisplayName());
    }

    @Nested
    class NotFoundMappings {

    /*
     * Test Case: shouldMapCustomerNotFoundExceptionToNotFound
     * Description: Validates shouldMapCustomerNotFoundExceptionToNotFound behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldMapCustomerNotFoundExceptionToNotFound() throws Exception {
            ResultActions result = mockMvc.perform(get("/test/exceptions/customer-not-found"));

            assertStandardErrorResponse(result, HttpStatus.NOT_FOUND, "/test/exceptions/customer-not-found");
            result.andExpect(jsonPath("$.message").value("Customer not found with id: 99"));
        }

    /*
     * Test Case: shouldMapAccountNotFoundExceptionToNotFound
     * Description: Validates shouldMapAccountNotFoundExceptionToNotFound behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldMapAccountNotFoundExceptionToNotFound() throws Exception {
            ResultActions result = mockMvc.perform(get("/test/exceptions/account-not-found"));

            assertStandardErrorResponse(result, HttpStatus.NOT_FOUND, "/test/exceptions/account-not-found");
            result.andExpect(jsonPath("$.message").value("Account not found with id: 55"));
        }
    }

    @Nested
    class ConflictMappings {

    /*
     * Test Case: shouldMapDuplicateEmailExceptionToConflict
     * Description: Validates shouldMapDuplicateEmailExceptionToConflict behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldMapDuplicateEmailExceptionToConflict() throws Exception {
            ResultActions result = mockMvc.perform(get("/test/exceptions/duplicate-email"));

            assertStandardErrorResponse(result, HttpStatus.CONFLICT, "/test/exceptions/duplicate-email");
            result.andExpect(jsonPath("$.message").value("Email already exists"));
        }
    }

    @Nested
    class ValidationAndBadRequestMappings {

    /*
     * Test Case: shouldMapValidationErrorToBadRequestForInvalidEmail
     * Description: Validates shouldMapValidationErrorToBadRequestForInvalidEmail behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldMapValidationErrorToBadRequestForInvalidEmail() throws Exception {
            String requestBody = """
                    {"name":"John Doe","email":"not-an-email","phone":"9876543210"}
                    """;

            ResultActions result = mockMvc.perform(post("/test/exceptions/validation")
                    .contentType(APPLICATION_JSON)
                    .content(requestBody));

            assertStandardErrorResponse(result, HttpStatus.BAD_REQUEST, "/test/exceptions/validation");
            result.andExpect(jsonPath("$.message").exists());
        }

    /*
     * Test Case: shouldMapValidationErrorToBadRequestForBlankName
     * Description: Validates shouldMapValidationErrorToBadRequestForBlankName behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldMapValidationErrorToBadRequestForBlankName() throws Exception {
            String requestBody = """
                    {"name":" ","email":"john@example.com","phone":"9876543210"}
                    """;

            ResultActions result = mockMvc.perform(post("/test/exceptions/validation")
                    .contentType(APPLICATION_JSON)
                    .content(requestBody));

            assertStandardErrorResponse(result, HttpStatus.BAD_REQUEST, "/test/exceptions/validation");
        }

    /*
     * Test Case: shouldMapValidationErrorToBadRequestForInvalidPhone
     * Description: Validates shouldMapValidationErrorToBadRequestForInvalidPhone behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldMapValidationErrorToBadRequestForInvalidPhone() throws Exception {
            String requestBody = """
                    {"name":"John Doe","email":"john@example.com","phone":"98A6543210"}
                    """;

            ResultActions result = mockMvc.perform(post("/test/exceptions/validation")
                    .contentType(APPLICATION_JSON)
                    .content(requestBody));

            assertStandardErrorResponse(result, HttpStatus.BAD_REQUEST, "/test/exceptions/validation");
        }

    /*
     * Test Case: shouldMapValidationErrorToBadRequestForMissingRequiredFields
     * Description: Validates shouldMapValidationErrorToBadRequestForMissingRequiredFields behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldMapValidationErrorToBadRequestForMissingRequiredFields() throws Exception {
            String requestBody = "{}";

            ResultActions result = mockMvc.perform(post("/test/exceptions/validation")
                    .contentType(APPLICATION_JSON)
                    .content(requestBody));

            assertStandardErrorResponse(result, HttpStatus.BAD_REQUEST, "/test/exceptions/validation");
        }

    /*
     * Test Case: shouldMapMultipleValidationErrorsToBadRequest
     * Description: Validates shouldMapMultipleValidationErrorsToBadRequest behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldMapMultipleValidationErrorsToBadRequest() throws Exception {
            String requestBody = """
                    {"name":" ","email":"bad","phone":"badphone"}
                    """;

            ResultActions result = mockMvc.perform(post("/test/exceptions/validation")
                    .contentType(APPLICATION_JSON)
                    .content(requestBody));

            assertStandardErrorResponse(result, HttpStatus.BAD_REQUEST, "/test/exceptions/validation");
            String body = result.andReturn().getResponse().getContentAsString();
            assertThat(body).contains("message").contains("status").contains("timestamp");
        }

    /*
     * Test Case: shouldMapIllegalArgumentExceptionToBadRequest
     * Description: Validates shouldMapIllegalArgumentExceptionToBadRequest behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldMapIllegalArgumentExceptionToBadRequest() throws Exception {
            ResultActions result = mockMvc.perform(get("/test/exceptions/illegal-argument"));

            assertStandardErrorResponse(result, HttpStatus.BAD_REQUEST, "/test/exceptions/illegal-argument");
            result.andExpect(jsonPath("$.message").value("Invalid argument"));
        }
    }

    @Nested
    class SecurityExceptionMappings {

    /*
     * Test Case: shouldMapAuthenticationExceptionToUnauthorized
     * Description: Validates shouldMapAuthenticationExceptionToUnauthorized behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldMapAuthenticationExceptionToUnauthorized() throws Exception {
            ResultActions result = mockMvc.perform(get("/test/exceptions/authentication"));

            assertStandardErrorResponse(result, HttpStatus.UNAUTHORIZED, "/test/exceptions/authentication");
            result.andExpect(jsonPath("$.message").value("Bad credentials"));
        }

    /*
     * Test Case: shouldMapAccessDeniedExceptionToForbidden
     * Description: Validates shouldMapAccessDeniedExceptionToForbidden behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldMapAccessDeniedExceptionToForbidden() throws Exception {
            ResultActions result = mockMvc.perform(get("/test/exceptions/access-denied"));

            assertStandardErrorResponse(result, HttpStatus.FORBIDDEN, "/test/exceptions/access-denied");
            result.andExpect(jsonPath("$.message").value("Access is denied"));
        }
    }

    @Nested
    class GenericExceptionMappings {

    /*
     * Test Case: shouldMapUnexpectedRuntimeExceptionToInternalServerError
     * Description: Validates shouldMapUnexpectedRuntimeExceptionToInternalServerError behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldMapUnexpectedRuntimeExceptionToInternalServerError() throws Exception {
            ResultActions result = mockMvc.perform(get("/test/exceptions/runtime"));

            assertStandardErrorResponse(result, HttpStatus.INTERNAL_SERVER_ERROR, "/test/exceptions/runtime");
            result.andExpect(jsonPath("$.message").value("Unexpected runtime error"));
        }

    /*
     * Test Case: shouldHandleNullExceptionMessageAsGenericError
     * Description: Validates shouldHandleNullExceptionMessageAsGenericError behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldHandleNullExceptionMessageAsGenericError() throws Exception {
            ResultActions result = mockMvc.perform(get("/test/exceptions/null-message"));

            assertStandardErrorResponse(result, HttpStatus.INTERNAL_SERVER_ERROR, "/test/exceptions/null-message");
            result.andExpect(jsonPath("$.message").exists());
        }

    /*
     * Test Case: shouldHandleNestedExceptionAsInternalServerError
     * Description: Validates shouldHandleNestedExceptionAsInternalServerError behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldHandleNestedExceptionAsInternalServerError() throws Exception {
            ResultActions result = mockMvc.perform(get("/test/exceptions/nested"));

            assertStandardErrorResponse(result, HttpStatus.INTERNAL_SERVER_ERROR, "/test/exceptions/nested");
            result.andExpect(jsonPath("$.message").value("Outer exception"));
        }

    /*
     * Test Case: shouldHandleUnknownExceptionAsInternalServerError
     * Description: Validates shouldHandleUnknownExceptionAsInternalServerError behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldHandleUnknownExceptionAsInternalServerError() throws Exception {
            ResultActions result = mockMvc.perform(get("/test/exceptions/unknown"));

            assertStandardErrorResponse(result, HttpStatus.INTERNAL_SERVER_ERROR, "/test/exceptions/unknown");
        }
    }

    private void assertStandardErrorResponse(ResultActions result, HttpStatus status, String path) throws Exception {
        result.andExpect(status().is(status.value()))
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(status.value()))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.path").value(path));
    }

    @RestController
    @RequestMapping("/test/exceptions")
    static class ExceptionThrowingController {

        @GetMapping("/customer-not-found")
        ResponseEntity<Void> customerNotFound() {
            throw new CustomerNotFoundException("Customer not found with id: 99");
        }

        @GetMapping("/account-not-found")
        ResponseEntity<Void> accountNotFound() {
            throw new AccountNotFoundException("Account not found with id: 55");
        }

        @GetMapping("/duplicate-email")
        ResponseEntity<Void> duplicateEmail() {
            throw new DuplicateEmailException("Email already exists");
        }

        @PostMapping("/validation")
        ResponseEntity<String> validation(@Valid @RequestBody ValidationRequest request) {
            return ResponseEntity.ok("valid");
        }

        @GetMapping("/illegal-argument")
        ResponseEntity<Void> illegalArgument() {
            throw new IllegalArgumentException("Invalid argument");
        }

        @GetMapping("/authentication")
        ResponseEntity<Void> authenticationException() {
            throw new org.springframework.security.authentication.BadCredentialsException("Bad credentials");
        }

        @GetMapping("/access-denied")
        ResponseEntity<Void> accessDeniedException() throws AccessDeniedException {
            throw new AccessDeniedException("Access is denied");
        }

        @GetMapping("/runtime")
        ResponseEntity<Void> runtimeException() {
            throw new RuntimeException("Unexpected runtime error");
        }

        @GetMapping("/null-message")
        ResponseEntity<Void> nullMessage() {
            throw new RuntimeException((String) null);
        }

        @GetMapping("/nested")
        ResponseEntity<Void> nestedException() {
            throw new RuntimeException("Outer exception", new IllegalStateException("Inner exception"));
        }

        @GetMapping("/unknown")
        ResponseEntity<Void> unknownException() {
            throw new UnsupportedOperationException("Unknown unsupported operation");
        }
    }

    static class ValidationRequest {

        @NotBlank(message = "Name is required")
        private String name;

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        private String email;

        @NotBlank(message = "Phone is required")
        @Pattern(regexp = "\\d{10}", message = "Phone must be 10 digits")
        private String phone;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }

}
