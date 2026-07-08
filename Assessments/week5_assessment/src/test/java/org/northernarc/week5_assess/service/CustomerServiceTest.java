package org.northernarc.week5_assess.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.northernarc.week5_assess.dto.CustomerRegistrationRequest;
import org.northernarc.week5_assess.dto.CustomerUpdateRequest;
import org.northernarc.week5_assess.entity.Customer;
import org.northernarc.week5_assess.exception.CustomerNotFoundException;
import org.northernarc.week5_assess.exception.DuplicateEmailException;
import org.northernarc.week5_assess.repository.CustomerRepository;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CustomerService customerService;

    private CustomerRegistrationRequest baseRegistrationRequest;
    private CustomerUpdateRequest baseUpdateRequest;
    private Customer baseCustomer;

    @BeforeEach
    void beforeEach(TestInfo testInfo) {
        baseRegistrationRequest = new CustomerRegistrationRequest();
        baseRegistrationRequest.setName("John Doe");
        baseRegistrationRequest.setEmail("john@example.com");
        baseRegistrationRequest.setPassword("pass@123");
        baseRegistrationRequest.setPhone("9876543210");

        baseUpdateRequest = new CustomerUpdateRequest();
        baseUpdateRequest.setName("John Updated");
        baseUpdateRequest.setEmail("john.updated@example.com");
        baseUpdateRequest.setPhone("9123456789");

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
     * Test Case: shouldRegisterCustomerSuccessfully
     * Description: Validates shouldRegisterCustomerSuccessfully behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldRegisterCustomerSuccessfully() {
            CustomerRegistrationRequest request = validRegistrationRequest();
            when(customerRepository.existsByEmail("john@example.com")).thenReturn(false);
            when(passwordEncoder.encode("pass@123")).thenReturn("encoded-pass");
            when(customerRepository.save(isA(Customer.class))).thenReturn(savedCustomer(1L));

            Customer result = customerService.registerCustomer(request);

            assertThat(result.getId()).isEqualTo(1L);
            assertThat(result.getEmail()).isEqualTo("john@example.com");
            verify(customerRepository).existsByEmail("john@example.com");
            verify(passwordEncoder, times(1)).encode("pass@123");
            verify(customerRepository, times(1)).save(any(Customer.class));
            verifyNoMoreInteractions(customerRepository, passwordEncoder);
        }

    /*
     * Test Case: shouldEncryptPasswordBeforeSaving
     * Description: Validates shouldEncryptPasswordBeforeSaving behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldEncryptPasswordBeforeSaving() {
            CustomerRegistrationRequest request = validRegistrationRequest();
            when(customerRepository.existsByEmail("john@example.com")).thenReturn(false);
            when(passwordEncoder.encode("pass@123")).thenReturn("encoded-pass");
            when(customerRepository.save(isA(Customer.class))).thenReturn(savedCustomer(1L));
            ArgumentCaptor<Customer> customerCaptor = ArgumentCaptor.forClass(Customer.class);

            customerService.registerCustomer(request);

            verify(customerRepository).save(customerCaptor.capture());
            assertThat(customerCaptor.getValue().getPassword()).isEqualTo("encoded-pass");
        }

    /*
     * Test Case: shouldThrowDuplicateEmailExceptionWhenEmailAlreadyExists
     * Description: Validates shouldThrowDuplicateEmailExceptionWhenEmailAlreadyExists behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldThrowDuplicateEmailExceptionWhenEmailAlreadyExists() {
            CustomerRegistrationRequest request = validRegistrationRequest();
            when(customerRepository.existsByEmail("john@example.com")).thenReturn(true);

            assertThrows(DuplicateEmailException.class, () -> customerService.registerCustomer(request));

            verify(customerRepository).existsByEmail("john@example.com");
            verify(passwordEncoder, never()).encode(any(CharSequence.class));
            verify(customerRepository, never()).save(any(Customer.class));
        }

    /*
     * Test Case: shouldFailWhenNameIsMandatory
     * Description: Validates shouldFailWhenNameIsMandatory behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldFailWhenNameIsMandatory() {
            CustomerRegistrationRequest request = validRegistrationRequest();
            request.setName(null);

            assertThrows(IllegalArgumentException.class, () -> customerService.registerCustomer(request));

            verify(customerRepository, never()).save(any(Customer.class));
        }

    /*
     * Test Case: shouldFailWhenEmailIsMandatory
     * Description: Validates shouldFailWhenEmailIsMandatory behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldFailWhenEmailIsMandatory() {
            CustomerRegistrationRequest request = validRegistrationRequest();
            request.setEmail(null);

            assertThrows(IllegalArgumentException.class, () -> customerService.registerCustomer(request));

            verify(customerRepository, never()).save(any(Customer.class));
        }

    /*
     * Test Case: shouldFailWhenPasswordIsMandatory
     * Description: Validates shouldFailWhenPasswordIsMandatory behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldFailWhenPasswordIsMandatory() {
            CustomerRegistrationRequest request = validRegistrationRequest();
            request.setPassword(null);

            assertThrows(IllegalArgumentException.class, () -> customerService.registerCustomer(request));

            verify(customerRepository, never()).save(any(Customer.class));
        }

    /*
     * Test Case: shouldFailWhenPhoneNumberIsMandatory
     * Description: Validates shouldFailWhenPhoneNumberIsMandatory behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldFailWhenPhoneNumberIsMandatory() {
            CustomerRegistrationRequest request = validRegistrationRequest();
            request.setPhone(" ");

            assertThrows(IllegalArgumentException.class, () -> customerService.registerCustomer(request));

            verify(customerRepository, never()).save(any(Customer.class));
        }

    /*
     * Test Case: shouldFailWhenEmailFormatIsInvalid
     * Description: Validates shouldFailWhenEmailFormatIsInvalid behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldFailWhenEmailFormatIsInvalid() {
            CustomerRegistrationRequest request = validRegistrationRequest();
            request.setEmail("john-email.com");

            assertThrows(IllegalArgumentException.class, () -> customerService.registerCustomer(request));

            verify(customerRepository, never()).save(any(Customer.class));
        }

    /*
     * Test Case: shouldFailWhenPhoneNumberLessThanTenDigits
     * Description: Validates shouldFailWhenPhoneNumberLessThanTenDigits behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldFailWhenPhoneNumberLessThanTenDigits() {
            CustomerRegistrationRequest request = validRegistrationRequest();
            request.setPhone("987654321");

            assertThrows(IllegalArgumentException.class, () -> customerService.registerCustomer(request));

            verify(customerRepository, never()).save(any(Customer.class));
        }

    /*
     * Test Case: shouldFailWhenPhoneNumberGreaterThanTenDigits
     * Description: Validates shouldFailWhenPhoneNumberGreaterThanTenDigits behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldFailWhenPhoneNumberGreaterThanTenDigits() {
            CustomerRegistrationRequest request = validRegistrationRequest();
            request.setPhone("98765432101");

            assertThrows(IllegalArgumentException.class, () -> customerService.registerCustomer(request));

            verify(customerRepository, never()).save(any(Customer.class));
        }

    /*
     * Test Case: shouldFailWhenPhoneNumberContainsAlphabets
     * Description: Validates shouldFailWhenPhoneNumberContainsAlphabets behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldFailWhenPhoneNumberContainsAlphabets() {
            CustomerRegistrationRequest request = validRegistrationRequest();
            request.setPhone("98765AB210");

            assertThrows(IllegalArgumentException.class, () -> customerService.registerCustomer(request));

            verify(customerRepository, never()).save(any(Customer.class));
        }

    /*
     * Test Case: shouldFailWhenPhoneNumberContainsSpecialCharacters
     * Description: Validates shouldFailWhenPhoneNumberContainsSpecialCharacters behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldFailWhenPhoneNumberContainsSpecialCharacters() {
            CustomerRegistrationRequest request = validRegistrationRequest();
            request.setPhone("98765-3210");

            assertThrows(IllegalArgumentException.class, () -> customerService.registerCustomer(request));

            verify(customerRepository, never()).save(any(Customer.class));
        }

    /*
     * Test Case: shouldFailWhenBlankNameProvided
     * Description: Validates shouldFailWhenBlankNameProvided behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldFailWhenBlankNameProvided() {
            CustomerRegistrationRequest request = validRegistrationRequest();
            request.setName(" ");

            assertThrows(IllegalArgumentException.class, () -> customerService.registerCustomer(request));
        }

    /*
     * Test Case: shouldFailWhenBlankEmailProvided
     * Description: Validates shouldFailWhenBlankEmailProvided behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldFailWhenBlankEmailProvided() {
            CustomerRegistrationRequest request = validRegistrationRequest();
            request.setEmail(" ");

            assertThrows(IllegalArgumentException.class, () -> customerService.registerCustomer(request));
        }

    /*
     * Test Case: shouldFailWhenBlankPasswordProvided
     * Description: Validates shouldFailWhenBlankPasswordProvided behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldFailWhenBlankPasswordProvided() {
            CustomerRegistrationRequest request = validRegistrationRequest();
            request.setPassword(" ");

            assertThrows(IllegalArgumentException.class, () -> customerService.registerCustomer(request));
        }

    /*
     * Test Case: shouldFailWhenRequestIsNull
     * Description: Validates shouldFailWhenRequestIsNull behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldFailWhenRequestIsNull() {
            assertThrows(IllegalArgumentException.class, () -> customerService.registerCustomer(null));

            verify(customerRepository, never()).save(any(Customer.class));
            verify(passwordEncoder, never()).encode(any(CharSequence.class));
        }

    /*
     * Test Case: shouldTrimInputBeforeSaving
     * Description: Validates shouldTrimInputBeforeSaving behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldTrimInputBeforeSaving() {
            CustomerRegistrationRequest request = validRegistrationRequest();
            request.setName("  John Doe  ");
            request.setEmail("  john@example.com  ");
            request.setPhone(" 9876543210 ");
            when(customerRepository.existsByEmail("john@example.com")).thenReturn(false);
            when(passwordEncoder.encode("pass@123")).thenReturn("encoded-pass");
            when(customerRepository.save(isA(Customer.class))).thenReturn(savedCustomer(2L));
            ArgumentCaptor<Customer> customerCaptor = ArgumentCaptor.forClass(Customer.class);

            customerService.registerCustomer(request);

            verify(customerRepository).save(customerCaptor.capture());
            assertThat(customerCaptor.getValue().getName()).isEqualTo("John Doe");
            assertThat(customerCaptor.getValue().getEmail()).isEqualTo("john@example.com");
            assertThat(customerCaptor.getValue().getPhone()).isEqualTo("9876543210");
        }

    /*
     * Test Case: shouldPropagateRepositoryExceptionOnSave
     * Description: Validates shouldPropagateRepositoryExceptionOnSave behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldPropagateRepositoryExceptionOnSave() {
            CustomerRegistrationRequest request = validRegistrationRequest();
            RuntimeException repositoryFailure = new RuntimeException("db failure");
            when(customerRepository.existsByEmail("john@example.com")).thenReturn(false);
            when(passwordEncoder.encode("pass@123")).thenReturn("encoded-pass");
            when(customerRepository.save(isA(Customer.class))).thenThrow(repositoryFailure);

            RuntimeException thrown = assertThrows(RuntimeException.class, () -> customerService.registerCustomer(request));
            assertThat(thrown).isSameAs(repositoryFailure);
        }
    }

    @Nested
    class CustomerCrud {

    /*
     * Test Case: shouldGetAllCustomersSuccessfully
     * Description: Validates shouldGetAllCustomersSuccessfully behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldGetAllCustomersSuccessfully() {
            when(customerRepository.findAll()).thenReturn(List.of(savedCustomer(1L), savedCustomer(2L)));

            List<Customer> customers = customerService.getAllCustomers();

            assertThat(customers).hasSize(2);
            verify(customerRepository).findAll();
            verifyNoMoreInteractions(customerRepository);
        }

    /*
     * Test Case: shouldGetCustomerByIdSuccessfully
     * Description: Validates shouldGetCustomerByIdSuccessfully behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldGetCustomerByIdSuccessfully() {
            when(customerRepository.findById(1L)).thenReturn(Optional.of(savedCustomer(1L)));

            Customer customer = customerService.getCustomerById(1L);

            assertThat(customer.getId()).isEqualTo(1L);
            verify(customerRepository).findById(1L);
        }

    /*
     * Test Case: shouldThrowCustomerNotFoundExceptionWhenCustomerIdInvalid
     * Description: Validates shouldThrowCustomerNotFoundExceptionWhenCustomerIdInvalid behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldThrowCustomerNotFoundExceptionWhenCustomerIdInvalid() {
            when(customerRepository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(CustomerNotFoundException.class, () -> customerService.getCustomerById(99L));

            verify(customerRepository).findById(99L);
        }

    /*
     * Test Case: shouldUpdateCustomerSuccessfully
     * Description: Validates shouldUpdateCustomerSuccessfully behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldUpdateCustomerSuccessfully() {
            Customer existing = savedCustomer(1L);
            CustomerUpdateRequest request = updateRequest();
            when(customerRepository.findById(1L)).thenReturn(Optional.of(existing));
            when(customerRepository.save(isA(Customer.class))).thenAnswer(invocation -> invocation.getArgument(0));

            Customer updated = customerService.updateCustomer(1L, request);

            assertThat(updated.getName()).isEqualTo("John Updated");
            assertThat(updated.getEmail()).isEqualTo("john.updated@example.com");
            verify(customerRepository).findById(1L);
            verify(customerRepository).save(any(Customer.class));
        }

    /*
     * Test Case: shouldThrowCustomerNotFoundExceptionWhenUpdatingNonExistingCustomer
     * Description: Validates shouldThrowCustomerNotFoundExceptionWhenUpdatingNonExistingCustomer behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldThrowCustomerNotFoundExceptionWhenUpdatingNonExistingCustomer() {
            when(customerRepository.findById(500L)).thenReturn(Optional.empty());

            assertThrows(CustomerNotFoundException.class, () -> customerService.updateCustomer(500L, updateRequest()));

            verify(customerRepository).findById(500L);
            verify(customerRepository, never()).save(any(Customer.class));
        }

    /*
     * Test Case: shouldDeleteCustomerSuccessfully
     * Description: Validates shouldDeleteCustomerSuccessfully behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldDeleteCustomerSuccessfully() {
            Customer existing = savedCustomer(1L);
            when(customerRepository.findById(1L)).thenReturn(Optional.of(existing));

            customerService.deleteCustomer(1L);

            verify(customerRepository).findById(1L);
            verify(customerRepository).delete(existing);
            verifyNoMoreInteractions(customerRepository);
        }

    /*
     * Test Case: shouldThrowCustomerNotFoundExceptionWhenDeletingNonExistingCustomer
     * Description: Validates shouldThrowCustomerNotFoundExceptionWhenDeletingNonExistingCustomer behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldThrowCustomerNotFoundExceptionWhenDeletingNonExistingCustomer() {
            when(customerRepository.findById(1000L)).thenReturn(Optional.empty());

            assertThrows(CustomerNotFoundException.class, () -> customerService.deleteCustomer(1000L));

            verify(customerRepository).findById(1000L);
            verify(customerRepository, never()).delete(any(Customer.class));
        }

    /*
     * Test Case: shouldFailWhenInvalidIdProvidedForLookup
     * Description: Validates shouldFailWhenInvalidIdProvidedForLookup behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
        @Test
        void shouldFailWhenInvalidIdProvidedForLookup() {
            assertThrows(IllegalArgumentException.class, () -> customerService.getCustomerById(0L));

            verify(customerRepository, never()).findById(anyLong());
        }
    }

    private CustomerRegistrationRequest validRegistrationRequest() {
        CustomerRegistrationRequest request = new CustomerRegistrationRequest();
        request.setName(baseRegistrationRequest.getName());
        request.setEmail(baseRegistrationRequest.getEmail());
        request.setPassword(baseRegistrationRequest.getPassword());
        request.setPhone(baseRegistrationRequest.getPhone());
        return request;
    }

    private CustomerUpdateRequest updateRequest() {
        CustomerUpdateRequest request = new CustomerUpdateRequest();
        request.setName(baseUpdateRequest.getName());
        request.setEmail(baseUpdateRequest.getEmail());
        request.setPhone(baseUpdateRequest.getPhone());
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
