package org.northernarc.jpa.service;

import org.northernarc.jpa.model.FixedDeposit;
import org.northernarc.jpa.repository.FixedDepositRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class FixedDepositServiceImpl implements FixedDepositService {

    @Autowired
    private FixedDepositRepo fixedDepositRepo;

    @Override
    public FixedDeposit save(FixedDeposit fixedDeposit) {
        return fixedDepositRepo.save(fixedDeposit);
    }

    @Override
    public FixedDeposit findById(int id) {
        return fixedDepositRepo.findById(id).get();
    }

    @Override
    public void deleteById(int id) {
        fixedDepositRepo.deleteById(id);
    }

    @Override
    public FixedDeposit update(int id, FixedDeposit fixedDeposit) {

        FixedDeposit fd =
                fixedDepositRepo.findById(id).get();

        fd.setCustomerName(
                fixedDeposit.getCustomerName());

        fd.setDepositAmount(
                fixedDeposit.getDepositAmount());

        fd.setInterestRate(
                fixedDeposit.getInterestRate());

        fd.setTenureMonths(
                fixedDeposit.getTenureMonths());

        fixedDepositRepo.save(fd);

        return fd;
    }

    @Override
    public Collection<FixedDeposit> findAll() {
        return fixedDepositRepo.findAll();
    }
}