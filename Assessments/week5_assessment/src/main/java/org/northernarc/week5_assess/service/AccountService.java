package org.northernarc.week5_assess.service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.northernarc.week5_assess.dto.AccountCreateRequest;
import org.northernarc.week5_assess.dto.AccountUpdateRequest;
import org.northernarc.week5_assess.entity.Account;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    public Account createAccount(AccountCreateRequest request) {
        return null;
    }

    public List<Account> getAllAccounts() {
        return Collections.emptyList();
    }

    public Account getAccountById(Long id) {
        return null;
    }

    public Account updateAccount(Long id, AccountUpdateRequest request) {
        return null;
    }

    public void deleteAccount(Long id) {
    }

    public Account deposit(Long accountId, BigDecimal amount) {
        return null;
    }

    public Account withdraw(Long accountId, BigDecimal amount) {
        return null;
    }

    public void transfer(Long fromAccountId, Long toAccountId, BigDecimal amount) {
    }
}

