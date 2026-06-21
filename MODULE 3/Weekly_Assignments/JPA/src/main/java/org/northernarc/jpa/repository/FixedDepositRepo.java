package org.northernarc.jpa.repository;

import org.northernarc.jpa.model.FixedDeposit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FixedDepositRepo extends JpaRepository<FixedDeposit, Integer> {
}
