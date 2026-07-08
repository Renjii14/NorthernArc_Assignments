package org.northernarc.week5_assess.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import jakarta.persistence.PersistenceException;
import org.northernarc.week5_assess.entity.Customer;

@DataJpaTest
@DisplayName("Customer repository behaviour")
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    private Customer baseCustomer;

    @BeforeEach
    void beforeEach(TestInfo testInfo) {
        baseCustomer = new Customer();
        baseCustomer.setName("John Doe");
        baseCustomer.setEmail("john@example.com");
        baseCustomer.setPhone("9876543210");

        System.out.println("Running test: " + testInfo.getDisplayName());
    }

    /*
     * Test Case: shouldSaveCustomerSuccessfully
     * Description: Validates shouldSaveCustomerSuccessfully behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
    @Test
    @DisplayName("Save customer successfully")
    void shouldSaveCustomerSuccessfully() {
        long countBeforeSave = customerRepository.count();
        Customer customer = newCustomer("John Doe", "john@example.com", "9876543210");

        Customer savedCustomer = customerRepository.saveAndFlush(customer);

        assertThat(savedCustomer.getId()).isNotNull();
        assertThat(savedCustomer.getName()).isEqualTo("John Doe");
        assertThat(savedCustomer.getEmail()).isEqualTo("john@example.com");
        assertThat(savedCustomer.getPhone()).isEqualTo("9876543210");
        assertThat(customerRepository.count()).isEqualTo(countBeforeSave + 1);
    }

    /*
     * Test Case: shouldReturnCustomerWhenIdExists
     * Description: Validates shouldReturnCustomerWhenIdExists behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
    @Test
    void shouldReturnCustomerWhenIdExists() {
        Customer savedCustomer = customerRepository.saveAndFlush(
                newCustomer("John Doe", "john@example.com", "9876543210"));

        Optional<Customer> result = customerRepository.findById(savedCustomer.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(savedCustomer.getId());
        assertThat(result.get().getName()).isEqualTo("John Doe");
        assertThat(result.get().getEmail()).isEqualTo("john@example.com");
        assertThat(result.get().getPhone()).isEqualTo("9876543210");
    }

    /*
     * Test Case: shouldReturnEmptyWhenIdDoesNotExist
     * Description: Validates shouldReturnEmptyWhenIdDoesNotExist behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
    @Test
    void shouldReturnEmptyWhenIdDoesNotExist() {
        Optional<Customer> result = customerRepository.findById(Long.MAX_VALUE);

        assertThat(result).isEmpty();
    }

    /*
     * Test Case: shouldReturnCustomerWhenEmailExists
     * Description: Validates shouldReturnCustomerWhenEmailExists behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
    @Test
    void shouldReturnCustomerWhenEmailExists() {
        customerRepository.saveAndFlush(newCustomer("John Doe", "john@example.com", "9876543210"));

        Optional<Customer> result = customerRepository.findByEmail("john@example.com");

        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("john@example.com");
        assertThat(result.get().getName()).isEqualTo("John Doe");
    }

    /*
     * Test Case: shouldReturnEmptyWhenEmailDoesNotExist
     * Description: Validates shouldReturnEmptyWhenEmailDoesNotExist behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
    @Test
    void shouldReturnEmptyWhenEmailDoesNotExist() {
        Optional<Customer> result = customerRepository.findByEmail("missing@example.com");

        assertThat(result).isEmpty();
    }

    /*
     * Test Case: shouldReturnTrueWhenEmailExists
     * Description: Validates shouldReturnTrueWhenEmailExists behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
    @Test
    void shouldReturnTrueWhenEmailExists() {
        customerRepository.saveAndFlush(newCustomer("John Doe", "john@example.com", "9876543210"));

        boolean exists = customerRepository.existsByEmail("john@example.com");

        assertThat(exists).isTrue();
    }

    /*
     * Test Case: shouldReturnFalseWhenEmailDoesNotExist
     * Description: Validates shouldReturnFalseWhenEmailDoesNotExist behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
    @Test
    void shouldReturnFalseWhenEmailDoesNotExist() {
        boolean exists = customerRepository.existsByEmail("unknown@example.com");

        assertThat(exists).isFalse();
    }

    /*
     * Test Case: shouldUpdateExistingCustomer
     * Description: Validates shouldUpdateExistingCustomer behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
    @Test
    void shouldUpdateExistingCustomer() {
        Customer savedCustomer = customerRepository.saveAndFlush(
                newCustomer("John Doe", "john@example.com", "9876543210"));

        savedCustomer.setName("John D");
        savedCustomer.setPhone("9998887776");
        Customer updatedCustomer = customerRepository.saveAndFlush(savedCustomer);

        Optional<Customer> result = customerRepository.findById(updatedCustomer.getId());
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("John D");
        assertThat(result.get().getPhone()).isEqualTo("9998887776");
        assertThat(result.get().getEmail()).isEqualTo("john@example.com");
    }

    /*
     * Test Case: shouldDeleteCustomerSuccessfully
     * Description: Validates shouldDeleteCustomerSuccessfully behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
    @Test
    void shouldDeleteCustomerSuccessfully() {
        Customer savedCustomer = customerRepository.saveAndFlush(
                newCustomer("John Doe", "john@example.com", "9876543210"));
        long countBeforeDelete = customerRepository.count();

        customerRepository.deleteById(savedCustomer.getId());
        customerRepository.flush();

        assertThat(customerRepository.count()).isEqualTo(countBeforeDelete - 1);
        assertThat(customerRepository.existsById(savedCustomer.getId())).isFalse();
        assertThat(customerRepository.findById(savedCustomer.getId())).isEmpty();
    }

    /*
     * Test Case: shouldFailWhenSavingCustomerWithDuplicateEmail
     * Description: Validates shouldFailWhenSavingCustomerWithDuplicateEmail behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
    @Test
    void shouldFailWhenSavingCustomerWithDuplicateEmail() {
        customerRepository.saveAndFlush(newCustomer("John Doe", "john@example.com", "9876543210"));
        Customer duplicateEmailCustomer = newCustomer("Jane Doe", "john@example.com", "9000011111");

        assertThatThrownBy(() -> customerRepository.saveAndFlush(duplicateEmailCustomer))
                .isInstanceOfAny(DataIntegrityViolationException.class, PersistenceException.class);
    }

    /*
     * Test Case: shouldReturnEmptyWhenFindByEmailIsNull
     * Description: Validates shouldReturnEmptyWhenFindByEmailIsNull behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
    @Test
    void shouldReturnEmptyWhenFindByEmailIsNull() {
        Optional<Customer> result = customerRepository.findByEmail(null);

        assertThat(result).isEmpty();
    }

    /*
     * Test Case: shouldReturnFalseWhenExistsByEmailIsNull
     * Description: Validates shouldReturnFalseWhenExistsByEmailIsNull behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
    @Test
    void shouldReturnFalseWhenExistsByEmailIsNull() {
        boolean exists = customerRepository.existsByEmail(null);

        assertThat(exists).isFalse();
    }

    /*
     * Test Case: shouldReturnEmptyWhenFindByEmailIsBlank
     * Description: Validates shouldReturnEmptyWhenFindByEmailIsBlank behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
    @Test
    void shouldReturnEmptyWhenFindByEmailIsBlank() {
        Optional<Customer> result = customerRepository.findByEmail("");

        assertThat(result).isEmpty();
    }

    /*
     * Test Case: shouldReturnEmptyWhenFindByEmailIsWhitespace
     * Description: Validates shouldReturnEmptyWhenFindByEmailIsWhitespace behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
    @Test
    void shouldReturnEmptyWhenFindByEmailIsWhitespace() {
        Optional<Customer> result = customerRepository.findByEmail(" ");

        assertThat(result).isEmpty();
    }

    /*
     * Test Case: shouldReturnCorrectCustomerWhenMultipleCustomersExist
     * Description: Validates shouldReturnCorrectCustomerWhenMultipleCustomersExist behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
    @Test
    void shouldReturnCorrectCustomerWhenMultipleCustomersExist() {
        customerRepository.saveAndFlush(newCustomer("John Doe", "john@example.com", "9876543210"));
        customerRepository.saveAndFlush(newCustomer("Jane Roe", "jane@example.com", "9123456789"));
        customerRepository.saveAndFlush(newCustomer("Sam Smith", "sam@example.com", "9345612780"));

        Optional<Customer> result = customerRepository.findByEmail("jane@example.com");

        assertThat(customerRepository.count()).isEqualTo(3);
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Jane Roe");
        assertThat(result.get().getEmail()).isEqualTo("jane@example.com");
    }

    /*
     * Test Case: shouldTrackRepositoryCountAfterSaveAndDelete
     * Description: Validates shouldTrackRepositoryCountAfterSaveAndDelete behavior.
     * Expected Outcome: Assertion(s) in this test should pass for expected behavior.
     */
    @Test
    void shouldTrackRepositoryCountAfterSaveAndDelete() {
        long initialCount = customerRepository.count();
        Customer savedCustomer = customerRepository.saveAndFlush(
                newCustomer("John Doe", "john@example.com", "9876543210"));

        long countAfterSave = customerRepository.count();

        customerRepository.delete(savedCustomer);
        customerRepository.flush();
        long countAfterDelete = customerRepository.count();

        assertThat(countAfterSave).isEqualTo(initialCount + 1);
        assertThat(countAfterDelete).isEqualTo(initialCount);
    }

    private Customer newCustomer(String name, String email, String phone) {
        Customer customer = new Customer();
        customer.setName(name != null ? name : baseCustomer.getName());
        customer.setEmail(email != null ? email : baseCustomer.getEmail());
        customer.setPhone(phone != null ? phone : baseCustomer.getPhone());
        return customer;
    }
}
