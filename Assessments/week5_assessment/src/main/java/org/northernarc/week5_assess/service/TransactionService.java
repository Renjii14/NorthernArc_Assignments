package org.northernarc.week5_assess.service;

import java.util.Collections;
import java.util.List;

import org.northernarc.week5_assess.entity.Transaction;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {
    public List<Transaction> getAllTransactions() {
        return Collections.emptyList();
    }

    public Transaction getTransactionById(Long id) {
        return null;
    }

    public List<Transaction> getTransactionsByAccountId(Long accountId) {
        return Collections.emptyList();
    }
}

