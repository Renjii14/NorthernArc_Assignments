package org.northernarc.week5_assess.repository;

import java.util.List;

import org.northernarc.week5_assess.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccountId(Long accountId);

    List<Transaction> findByAccountIdOrderByCreatedAtDesc(Long accountId);
}
