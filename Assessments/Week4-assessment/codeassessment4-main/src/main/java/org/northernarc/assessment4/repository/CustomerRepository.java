package org.northernarc.assessment4.repository;

import org.northernarc.assessment4.dto.CustomerSummaryDTO;
import org.northernarc.assessment4.model.Customer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // Task 3: Derived Query Method
    List<Customer> findByBranch(String branch);

    // Task 4: Find Rich Customers Query
    //Return customers whose balance exceeds a given amount provided in path or in query string.
    @Query("""
            SELECT c
            FROM Customer c
            JOIN c.accounts a
            GROUP BY c
            HAVING SUM(a.balance) > :threshold
            """)
    List<Customer> findRichCustomers(@Param("threshold") double threshold);

    // Task 4: Find Total Balance Per Branch Query
    @Query("""
            SELECT c.branch, SUM(a.balance)
            FROM Customer c
            JOIN c.accounts a
            GROUP BY c.branch
            """)
    List<Object[]> findTotalBalancePerBranch();

    // Task 4: Find Customers Having Multiple Accounts Query
    @Query("""
            SELECT c
            FROM Customer c
            JOIN c.accounts a
            GROUP BY c
            HAVING COUNT(a) > 1
            """)
    List<Customer> findCustomersWithMultipleAccounts();

    // Task 7: Customer Summary DTO Projection Query
    @Query("""
            SELECT new org.northernarc.assessment4.dto.CustomerSummaryDTO(
                c.customerName,
                c.branch,
                COUNT(a),
                COALESCE(SUM(a.balance), 0.0)
            )
            FROM Customer c
            LEFT JOIN c.accounts a
            WHERE c.customerId = :customerId
            GROUP BY c.customerId, c.customerName, c.branch
            """)
    Optional<CustomerSummaryDTO> findCustomerSummaryById(@Param("customerId") Long customerId);

    // Final Challenge: Find branch with highest total balance
    @Query("""
            SELECT c.branch
            FROM Customer c
            JOIN c.accounts a
            GROUP BY c.branch
            ORDER BY SUM(a.balance) DESC, c.branch ASC
            """)
    List<String> findTopBranchByTotalBalance(Pageable pageable);

    // Final Challenge: Find customer with highest total balance
    @Query("""
            SELECT c.customerName
            FROM Customer c
            JOIN c.accounts a
            GROUP BY c.customerId, c.customerName
            ORDER BY SUM(a.balance) DESC, c.customerId ASC
            """)
    List<String> findHighestBalanceCustomer(Pageable pageable);

    // Security Helper
    Optional<Customer> findByEmail(String email);
}
