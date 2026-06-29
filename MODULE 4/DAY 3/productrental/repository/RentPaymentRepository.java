package org.northernarc.productrental.repository;

import java.util.List;
import org.northernarc.productrental.model.RentPayment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentPaymentRepository extends JpaRepository<RentPayment, Long> {
    List<RentPayment> findByRentalRecordRentalId(Long rentalId);

    @Query("select p from RentPayment p order by p.paymentDate desc, p.paymentId desc")
    List<RentPayment> findLatestRentPayment(Pageable pageable);
    @Query("""
SELECT SUM(rp.amount)
FROM RentPayment rp
""")
    Double findTotalRentCollected();
}
