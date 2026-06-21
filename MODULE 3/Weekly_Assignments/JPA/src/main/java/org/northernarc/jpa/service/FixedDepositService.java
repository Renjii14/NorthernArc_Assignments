package org.northernarc.jpa.service;

import org.northernarc.jpa.model.FixedDeposit;

import java.util.Collection;

public interface FixedDepositService {

    FixedDeposit save(FixedDeposit fixedDeposit);

    FixedDeposit findById(int id);

    Collection<FixedDeposit> findAll();

    void deleteById(int id);

    FixedDeposit update(
            int id,
            FixedDeposit fixedDeposit);
}