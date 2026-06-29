package org.northernarc.productrental.repository;

import java.time.LocalDate;
import java.util.List;
import org.northernarc.productrental.model.RentalRecord;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentalRecordRepository extends JpaRepository<RentalRecord, Long> {
    List<RentalRecord> findByCustomerCustomerId(Long customerId);
    List<RentalRecord> findByProductProductId(Long productId);
    List<RentalRecord> findByStatus(String status);
    List<RentalRecord> findByStatusAndExpectedReturnDateBefore(String status, LocalDate date);
    long countByStatus(String status);
}
