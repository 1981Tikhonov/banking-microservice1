package com.bank.project.service;

import com.bank.project.entity.Account;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;

public class        AccountServiceImpl {
    private Account testAccount;

    public Account createAccount(Account testAccount) {
        this.testAccount = testAccount;
        return null;
    }

    public Account getAccountById(long l) {
            return null;
    }

    public Page<Account> getAllAccounts(int i, int i1, String[] strings) {
                return null;
    }

    public List<Account> getAccountsByClientId(long l) {
                        return null;
    }

    public Account updateAccount(long l, Account updatedAccount) {
        return null;
    }

    public boolean deleteAccount(long l) {
            return false;
    }

    public Account deposit(long l, BigDecimal bigDecimal) {
                    return null;
    }

    public Account withdraw(long l, BigDecimal bigDecimal) {
                        return null;
    }

    public void transfer(long l, long l1, BigDecimal bigDecimal) {

    }
}
