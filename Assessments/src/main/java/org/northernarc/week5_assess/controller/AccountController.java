package org.northernarc.week5_assess.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.northernarc.week5_assess.dto.AccountCreateRequest;
import org.northernarc.week5_assess.dto.AccountUpdateRequest;
import org.northernarc.week5_assess.entity.Account;
import org.northernarc.week5_assess.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<Account> create(@RequestBody AccountCreateRequest request) {
        return ResponseEntity.status(201).body(accountService.createAccount(request));
    }

    @GetMapping
    public ResponseEntity<List<Account>> getAll() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getById(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.getAccountById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Account> update(@PathVariable Long id, @RequestBody AccountUpdateRequest request) {
        return ResponseEntity.ok(accountService.updateAccount(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/deposit")
    public ResponseEntity<Account> deposit(@RequestBody Map<String, Object> request) {
        Long accountId = request.get("accountId") == null ? null : Long.valueOf(String.valueOf(request.get("accountId")));
        BigDecimal amount = request.get("amount") == null ? null : new BigDecimal(String.valueOf(request.get("amount")));
        return ResponseEntity.ok(accountService.deposit(accountId, amount));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Account> withdraw(@RequestBody Map<String, Object> request) {
        Long accountId = request.get("accountId") == null ? null : Long.valueOf(String.valueOf(request.get("accountId")));
        BigDecimal amount = request.get("amount") == null ? null : new BigDecimal(String.valueOf(request.get("amount")));
        return ResponseEntity.ok(accountService.withdraw(accountId, amount));
    }

    @PostMapping("/transfer")
    public ResponseEntity<Void> transfer(@RequestBody Map<String, Object> request) {
        Long fromAccountId = request.get("fromAccountId") == null ? null
                : Long.valueOf(String.valueOf(request.get("fromAccountId")));
        Long toAccountId = request.get("toAccountId") == null ? null : Long.valueOf(String.valueOf(request.get("toAccountId")));
        BigDecimal amount = request.get("amount") == null ? null : new BigDecimal(String.valueOf(request.get("amount")));
        accountService.transfer(fromAccountId, toAccountId, amount);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/admin-only")
    public ResponseEntity<Void> adminOnly() {
        return ResponseEntity.ok().build();
    }
}

