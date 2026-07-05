package org.northernarc.loanemi.repository;

import java.util.List;

import org.northernarc.loanemi.model.EmiPayment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EmiPaymentRepository extends JpaRepository<EmiPayment, Long> {

    EmiPayment findByReferenceNumber(String referenceNumber);

    @Query("select p from EmiPayment p order by p.paymentDate desc, p.paymentId desc")
    List<EmiPayment> findLatestPayment(Pageable pageable);
}
