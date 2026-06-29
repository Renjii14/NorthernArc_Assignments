package org.northernarc.productrental.repository;

import java.util.List;
import org.northernarc.productrental.model.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(String category);
    List<Product> findByAvailable(Boolean available);
    List<Product> findByRentPerDayGreaterThan(double amount);

    @Query("""
            select distinct p
            from Product p
            left join p.rentalRecords r on r.status = 'OVERDUE'
            where r is null
            """)
    List<Product> findProductsWithNoOverdueRentals();

    @Query("""
            select distinct p
            from Product p
            join p.rentalRecords r
            where r.status = 'OVERDUE'
            """)
    List<Product> findProductsCurrentlyOverdue();

    @Query("""
            select p.category
            from RentalRecord r
            join r.product p
            group by p.category
            order by count(r) desc, p.category asc
            """)
    List<String> findTopCategory(Pageable pageable);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("""
            update Product p
            set p.rentPerDay = p.rentPerDay + :increment
            where p.category = :category
            """)
    int increaseRentPerDay(@Param("category") String category, @Param("increment") double increment);


    @Query("""
SELECT p.category
FROM RentPayment rp
JOIN rp.rentalRecord rr
JOIN rr.product p
GROUP BY p.category
ORDER BY SUM(rp.amount) DESC
""")
    List<String> findTopCategoryByRevenue(Pageable pageable);
}
