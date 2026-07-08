package org.northernarc.week5_assess.controller;

import java.util.List;

import org.northernarc.week5_assess.entity.Transaction;
import org.northernarc.week5_assess.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/api/transactions")
    public ResponseEntity<List<Transaction>> getAll() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    @GetMapping("/api/transactions/{id}")
    public ResponseEntity<Transaction> getById(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.getTransactionById(id));
    }

    @GetMapping("/api/accounts/{id}/transactions")
    public ResponseEntity<List<Transaction>> getByAccountId(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.getTransactionsByAccountId(id));
    }
}

