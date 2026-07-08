package org.northernarc.week5_assess.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.northernarc.week5_assess.dto.AuthResponse;
import org.northernarc.week5_assess.dto.LoginRequest;
import org.northernarc.week5_assess.dto.RegisterRequest;
import org.northernarc.week5_assess.entity.Customer;
import org.northernarc.week5_assess.exception.DuplicateEmailException;
import org.northernarc.week5_assess.exception.GlobalExceptionHandler;
import org.northernarc.week5_assess.exception.UserNotFoundException;
import org.northernarc.week5_assess.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(AuthenticationController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
@DisplayName("Authentication controller API behaviour")
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationService authenticationService;

    private RegisterRequest baseRegisterRequest;
    private LoginRequest baseLoginRequest;
    private Customer baseCustomer;

    @BeforeEach
    void beforeEach(TestInfo testInfo) {
        baseRegisterRequest = new RegisterRequest();
        baseRegisterRequest.setName("John Doe");
        baseRegisterRequest.setEmail("john@example.com");
        baseRegisterRequest.setPassword("pass@123");
        baseRegisterRequest.setPhone("9876543210");

        baseLoginRequest = new LoginRequest();
        baseLoginRequest.setEmail("john@example.com");
        baseLoginRequest.setPassword("pass@123");

        baseCustomer = new Customer();
        baseCustomer.setId(1L);
        baseCustomer.setName("John Doe");
        baseCustomer.setEmail("john@example.com");
        baseCustomer.setPhone("9876543210");

        System.out.println("Running test: " + testInfo.getDisplayName());
    }

    @Nested
    class Register {

    /*
     * Test Case: shouldRegisterCustomerSuccessfully
     * Description: Validates shouldRegisterCustomerSuccessfully behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldRegisterCustomerSuccessfully() throws Exception {
            when(authenticationService.register(isA(RegisterRequest.class)))
                    .thenReturn(customer(1L, "John Doe", "john@example.com", "9876543210"));

            mockMvc.perform(post("/api/auth/register")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRegisterRequest())))
                    .andExpect(status().isCreated())
                    .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.name").value("John Doe"))
                    .andExpect(jsonPath("$.email").value("john@example.com"))
                    .andExpect(jsonPath("$.phone").value("9876543210"));

            verify(authenticationService).register(any(RegisterRequest.class));
        }

    /*
     * Test Case: shouldReturnConflictWhenDuplicateEmailDuringRegistration
     * Description: Validates shouldReturnConflictWhenDuplicateEmailDuringRegistration behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnConflictWhenDuplicateEmailDuringRegistration() throws Exception {
            when(authenticationService.register(isA(RegisterRequest.class)))
                    .thenThrow(new DuplicateEmailException("duplicate email"));

            mockMvc.perform(post("/api/auth/register")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRegisterRequest())))
                    .andExpect(status().isConflict());

            verify(authenticationService).register(any(RegisterRequest.class));
        }

    /*
     * Test Case: shouldReturnBadRequestForInvalidEmailDuringRegistration
     * Description: Validates shouldReturnBadRequestForInvalidEmailDuringRegistration behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnBadRequestForInvalidEmailDuringRegistration() throws Exception {
            RegisterRequest request = validRegisterRequest();
            request.setEmail("invalid-email");

            mockMvc.perform(post("/api/auth/register")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(authenticationService);
        }

    /*
     * Test Case: shouldReturnBadRequestWhenNameMissingDuringRegistration
     * Description: Validates shouldReturnBadRequestWhenNameMissingDuringRegistration behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnBadRequestWhenNameMissingDuringRegistration() throws Exception {
            RegisterRequest request = validRegisterRequest();
            request.setName(null);

            mockMvc.perform(post("/api/auth/register")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(authenticationService);
        }

    /*
     * Test Case: shouldReturnBadRequestWhenPasswordMissingDuringRegistration
     * Description: Validates shouldReturnBadRequestWhenPasswordMissingDuringRegistration behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnBadRequestWhenPasswordMissingDuringRegistration() throws Exception {
            RegisterRequest request = validRegisterRequest();
            request.setPassword(null);

            mockMvc.perform(post("/api/auth/register")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(authenticationService);
        }

    /*
     * Test Case: shouldReturnBadRequestWhenPhoneMissingDuringRegistration
     * Description: Validates shouldReturnBadRequestWhenPhoneMissingDuringRegistration behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnBadRequestWhenPhoneMissingDuringRegistration() throws Exception {
            RegisterRequest request = validRegisterRequest();
            request.setPhone(null);

            mockMvc.perform(post("/api/auth/register")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(authenticationService);
        }

    /*
     * Test Case: shouldReturnBadRequestWhenPhoneLessThanTenDigits
     * Description: Validates shouldReturnBadRequestWhenPhoneLessThanTenDigits behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnBadRequestWhenPhoneLessThanTenDigits() throws Exception {
            RegisterRequest request = validRegisterRequest();
            request.setPhone("987654321");

            mockMvc.perform(post("/api/auth/register")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(authenticationService);
        }

    /*
     * Test Case: shouldReturnBadRequestWhenPhoneGreaterThanTenDigits
     * Description: Validates shouldReturnBadRequestWhenPhoneGreaterThanTenDigits behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnBadRequestWhenPhoneGreaterThanTenDigits() throws Exception {
            RegisterRequest request = validRegisterRequest();
            request.setPhone("98765432101");

            mockMvc.perform(post("/api/auth/register")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(authenticationService);
        }

    /*
     * Test Case: shouldReturnBadRequestWhenBlankRegisterFields
     * Description: Validates shouldReturnBadRequestWhenBlankRegisterFields behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnBadRequestWhenBlankRegisterFields() throws Exception {
            RegisterRequest request = validRegisterRequest();
            request.setName(" ");
            request.setEmail(" ");
            request.setPassword(" ");
            request.setPhone(" ");

            mockMvc.perform(post("/api/auth/register")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(authenticationService);
        }

    /*
     * Test Case: shouldReturnBadRequestForNullRequestBody
     * Description: Validates shouldReturnBadRequestForNullRequestBody behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnBadRequestForNullRequestBody() throws Exception {
            mockMvc.perform(post("/api/auth/register")
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(authenticationService);
        }

    /*
     * Test Case: shouldReturnBadRequestForMalformedJsonOnRegister
     * Description: Validates shouldReturnBadRequestForMalformedJsonOnRegister behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnBadRequestForMalformedJsonOnRegister() throws Exception {
            mockMvc.perform(post("/api/auth/register")
                            .contentType(APPLICATION_JSON)
                            .content("{\"email\":\"john@example.com\""))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(authenticationService);
        }

    /*
     * Test Case: shouldReturnUnsupportedMediaTypeForRegister
     * Description: Validates shouldReturnUnsupportedMediaTypeForRegister behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnUnsupportedMediaTypeForRegister() throws Exception {
            mockMvc.perform(post("/api/auth/register")
                            .contentType(MediaType.TEXT_PLAIN)
                            .content("invalid"))
                    .andExpect(status().isUnsupportedMediaType());

            verifyNoInteractions(authenticationService);
        }
    }

    @Nested
    class Login {

    /*
     * Test Case: shouldLoginSuccessfully
     * Description: Validates shouldLoginSuccessfully behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldLoginSuccessfully() throws Exception {
            when(authenticationService.login(isA(LoginRequest.class))).thenReturn(token("jwt-token-value"));

            mockMvc.perform(post("/api/auth/login")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validLoginRequest())))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                    .andExpect(jsonPath("$.token").value("jwt-token-value"));

            verify(authenticationService).login(any(LoginRequest.class));
        }

    /*
     * Test Case: shouldReturnUnauthorizedForInvalidCredentials
     * Description: Validates shouldReturnUnauthorizedForInvalidCredentials behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnUnauthorizedForInvalidCredentials() throws Exception {
            when(authenticationService.login(isA(LoginRequest.class)))
                    .thenThrow(new BadCredentialsException("Invalid credentials"));

            mockMvc.perform(post("/api/auth/login")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validLoginRequest())))
                    .andExpect(status().isUnauthorized());

            verify(authenticationService).login(any(LoginRequest.class));
        }

    /*
     * Test Case: shouldReturnUnauthorizedForUnknownEmail
     * Description: Validates shouldReturnUnauthorizedForUnknownEmail behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnUnauthorizedForUnknownEmail() throws Exception {
            when(authenticationService.login(isA(LoginRequest.class)))
                    .thenThrow(new UserNotFoundException("User not found"));

            mockMvc.perform(post("/api/auth/login")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validLoginRequest())))
                    .andExpect(status().isUnauthorized());

            verify(authenticationService).login(any(LoginRequest.class));
        }

    /*
     * Test Case: shouldReturnBadRequestWhenEmailMissingInLogin
     * Description: Validates shouldReturnBadRequestWhenEmailMissingInLogin behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnBadRequestWhenEmailMissingInLogin() throws Exception {
            LoginRequest request = validLoginRequest();
            request.setEmail(null);

            mockMvc.perform(post("/api/auth/login")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(authenticationService);
        }

    /*
     * Test Case: shouldReturnBadRequestWhenPasswordMissingInLogin
     * Description: Validates shouldReturnBadRequestWhenPasswordMissingInLogin behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnBadRequestWhenPasswordMissingInLogin() throws Exception {
            LoginRequest request = validLoginRequest();
            request.setPassword(null);

            mockMvc.perform(post("/api/auth/login")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(authenticationService);
        }

    /*
     * Test Case: shouldReturnBadRequestWhenBlankEmailOrPasswordInLogin
     * Description: Validates shouldReturnBadRequestWhenBlankEmailOrPasswordInLogin behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnBadRequestWhenBlankEmailOrPasswordInLogin() throws Exception {
            LoginRequest request = validLoginRequest();
            request.setEmail(" ");
            request.setPassword(" ");

            mockMvc.perform(post("/api/auth/login")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(authenticationService);
        }

    /*
     * Test Case: shouldReturnBadRequestForNullLoginRequestBody
     * Description: Validates shouldReturnBadRequestForNullLoginRequestBody behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnBadRequestForNullLoginRequestBody() throws Exception {
            mockMvc.perform(post("/api/auth/login")
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(authenticationService);
        }

    /*
     * Test Case: shouldReturnBadRequestForMalformedJsonOnLogin
     * Description: Validates shouldReturnBadRequestForMalformedJsonOnLogin behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnBadRequestForMalformedJsonOnLogin() throws Exception {
            mockMvc.perform(post("/api/auth/login")
                            .contentType(APPLICATION_JSON)
                            .content("{\"email\":\"john@example.com\""))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(authenticationService);
        }

    /*
     * Test Case: shouldReturnUnsupportedMediaTypeForLogin
     * Description: Validates shouldReturnUnsupportedMediaTypeForLogin behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnUnsupportedMediaTypeForLogin() throws Exception {
            mockMvc.perform(post("/api/auth/login")
                            .contentType(MediaType.TEXT_PLAIN)
                            .content("invalid"))
                    .andExpect(status().isUnsupportedMediaType());

            verifyNoInteractions(authenticationService);
        }
    }

    private RegisterRequest validRegisterRequest() {
        RegisterRequest request = new RegisterRequest();
        request.setName(baseRegisterRequest.getName());
        request.setEmail(baseRegisterRequest.getEmail());
        request.setPassword(baseRegisterRequest.getPassword());
        request.setPhone(baseRegisterRequest.getPhone());
        return request;
    }

    private LoginRequest validLoginRequest() {
        LoginRequest request = new LoginRequest();
        request.setEmail(baseLoginRequest.getEmail());
        request.setPassword(baseLoginRequest.getPassword());
        return request;
    }

    private AuthResponse token(String value) {
        AuthResponse response = new AuthResponse();
        response.setToken(value);
        return response;
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
