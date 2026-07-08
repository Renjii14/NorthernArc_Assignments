package org.northernarc.week5_assess.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.northernarc.week5_assess.dto.AuthResponse;
import org.northernarc.week5_assess.dto.LoginRequest;
import org.northernarc.week5_assess.dto.RegisterRequest;
import org.northernarc.week5_assess.entity.Customer;
import org.northernarc.week5_assess.exception.DuplicateEmailException;
import org.northernarc.week5_assess.exception.UserNotFoundException;
import org.northernarc.week5_assess.repository.CustomerRepository;
import org.northernarc.week5_assess.security.JwtService;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private Authentication authentication;

    @InjectMocks
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
        baseCustomer.setName("John Doe");
        baseCustomer.setEmail("john@example.com");
        baseCustomer.setPassword("encoded-pass");
        baseCustomer.setPhone("9876543210");

        System.out.println("Running test: " + testInfo.getDisplayName());
    }

    @Nested
    class Registration {

    /*
     * Test Case: shouldRegisterSuccessfully
     * Description: Validates shouldRegisterSuccessfully behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldRegisterSuccessfully() {
            RegisterRequest request = validRegisterRequest();
            when(customerRepository.existsByEmail("john@example.com")).thenReturn(false);
            when(passwordEncoder.encode("pass@123")).thenReturn("encoded-pass");
            when(customerRepository.save(isA(Customer.class))).thenReturn(savedCustomer(1L));

            Customer result = authenticationService.register(request);

            assertThat(result.getId()).isEqualTo(1L);
            verify(customerRepository).existsByEmail("john@example.com");
            verify(passwordEncoder, times(1)).encode("pass@123");
            verify(customerRepository, times(1)).save(any(Customer.class));
            verifyNoMoreInteractions(customerRepository, passwordEncoder);
        }

    /*
     * Test Case: shouldFailWhenDuplicateEmailProvided
     * Description: Validates shouldFailWhenDuplicateEmailProvided behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldFailWhenDuplicateEmailProvided() {
            RegisterRequest request = validRegisterRequest();
            when(customerRepository.existsByEmail("john@example.com")).thenReturn(true);

            assertThrows(DuplicateEmailException.class, () -> authenticationService.register(request));

            verify(customerRepository).existsByEmail("john@example.com");
            verify(passwordEncoder, never()).encode(any(CharSequence.class));
            verify(customerRepository, never()).save(any(Customer.class));
        }

    /*
     * Test Case: shouldEncryptPasswordBeforeSaving
     * Description: Validates shouldEncryptPasswordBeforeSaving behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldEncryptPasswordBeforeSaving() {
            RegisterRequest request = validRegisterRequest();
            ArgumentCaptor<Customer> customerCaptor = ArgumentCaptor.forClass(Customer.class);
            when(customerRepository.existsByEmail("john@example.com")).thenReturn(false);
            when(passwordEncoder.encode("pass@123")).thenReturn("encoded-pass");
            when(customerRepository.save(isA(Customer.class))).thenReturn(savedCustomer(1L));

            authenticationService.register(request);

            verify(customerRepository).save(customerCaptor.capture());
            assertThat(customerCaptor.getValue().getPassword()).isEqualTo("encoded-pass");
        }

    /*
     * Test Case: shouldFailWhenRegisterRequestIsNull
     * Description: Validates shouldFailWhenRegisterRequestIsNull behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldFailWhenRegisterRequestIsNull() {
            assertThrows(IllegalArgumentException.class, () -> authenticationService.register(null));

            verifyNoMoreInteractions(customerRepository, passwordEncoder, authenticationManager, jwtService);
        }

    /*
     * Test Case: shouldFailWhenRegisterRequestContainsBlankValues
     * Description: Validates shouldFailWhenRegisterRequestContainsBlankValues behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldFailWhenRegisterRequestContainsBlankValues() {
            RegisterRequest request = validRegisterRequest();
            request.setEmail(" ");
            request.setPassword("");

            assertThrows(IllegalArgumentException.class, () -> authenticationService.register(request));

            verify(customerRepository, never()).save(any(Customer.class));
            verify(passwordEncoder, never()).encode(any(CharSequence.class));
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
        void shouldLoginSuccessfully() {
            LoginRequest request = validLoginRequest();
            Customer customer = savedCustomer(1L);
            when(customerRepository.findByEmail("john@example.com")).thenReturn(Optional.of(customer));
            when(authenticationManager.authenticate(isA(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(authentication);
            when(jwtService.generateToken(customer)).thenReturn("jwt-token");

            AuthResponse response = authenticationService.login(request);

            assertThat(response.getToken()).isEqualTo("jwt-token");
            verify(customerRepository).findByEmail("john@example.com");
            verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
            verify(jwtService).generateToken(customer);
        }

    /*
     * Test Case: shouldFailLoginWhenInvalidPassword
     * Description: Validates shouldFailLoginWhenInvalidPassword behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldFailLoginWhenInvalidPassword() {
            LoginRequest request = validLoginRequest();
            Customer customer = savedCustomer(1L);
            when(customerRepository.findByEmail("john@example.com")).thenReturn(Optional.of(customer));
            when(authenticationManager.authenticate(isA(UsernamePasswordAuthenticationToken.class)))
                    .thenThrow(new BadCredentialsException("Invalid credentials"));

            assertThrows(BadCredentialsException.class, () -> authenticationService.login(request));

            verify(customerRepository).findByEmail("john@example.com");
            verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
            verify(jwtService, never()).generateToken(any(Customer.class));
        }

    /*
     * Test Case: shouldFailLoginWhenInvalidEmailFormat
     * Description: Validates shouldFailLoginWhenInvalidEmailFormat behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldFailLoginWhenInvalidEmailFormat() {
            LoginRequest request = validLoginRequest();
            request.setEmail("john-email.com");

            assertThrows(IllegalArgumentException.class, () -> authenticationService.login(request));

            verifyNoMoreInteractions(customerRepository, authenticationManager, jwtService);
        }

    /*
     * Test Case: shouldFailLoginWhenUserNotFound
     * Description: Validates shouldFailLoginWhenUserNotFound behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldFailLoginWhenUserNotFound() {
            LoginRequest request = validLoginRequest();
            when(customerRepository.findByEmail("john@example.com")).thenReturn(Optional.empty());

            assertThrows(UserNotFoundException.class, () -> authenticationService.login(request));

            verify(customerRepository).findByEmail("john@example.com");
            verify(authenticationManager, never()).authenticate(any(Authentication.class));
            verify(jwtService, never()).generateToken(any(Customer.class));
        }

    /*
     * Test Case: shouldReturnJwtTokenInLoginResponse
     * Description: Validates shouldReturnJwtTokenInLoginResponse behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldReturnJwtTokenInLoginResponse() {
            LoginRequest request = validLoginRequest();
            Customer customer = savedCustomer(1L);
            when(customerRepository.findByEmail("john@example.com")).thenReturn(Optional.of(customer));
            when(authenticationManager.authenticate(isA(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(authentication);
            when(jwtService.generateToken(customer)).thenReturn("token-value");

            AuthResponse response = authenticationService.login(request);

            assertThat(response).isNotNull();
            assertThat(response.getToken()).isEqualTo("token-value");
        }

    /*
     * Test Case: shouldFailWhenLoginRequestIsNull
     * Description: Validates shouldFailWhenLoginRequestIsNull behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldFailWhenLoginRequestIsNull() {
            assertThrows(IllegalArgumentException.class, () -> authenticationService.login(null));

            verifyNoMoreInteractions(customerRepository, authenticationManager, jwtService);
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

    private Customer savedCustomer(Long id) {
        Customer customer = new Customer();
        customer.setId(id);
        customer.setName(baseCustomer.getName());
        customer.setEmail(baseCustomer.getEmail());
        customer.setPassword(baseCustomer.getPassword());
        customer.setPhone(baseCustomer.getPhone());
        return customer;
    }
}
