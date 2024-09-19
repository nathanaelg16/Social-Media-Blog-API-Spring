package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.exception.AccountRegistrationException;
import com.example.exception.DuplicateAccountRegistrationException;
import com.example.repository.AccountRepository;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    public Account registerAccount(Account account) throws AccountRegistrationException {
        if (account.getUsername() == null || account.getUsername().isBlank()) 
            throw new AccountRegistrationException("Invalid username");
        if (account.getPassword() == null || account.getPassword().length() < 4)
            throw new AccountRegistrationException("Invalid password");
        if (accountRepository.findByUsername(account.getUsername()) != null)
            throw new DuplicateAccountRegistrationException();
        return accountRepository.save(account);
    }

    public Account login(Account account) {
        if (account.getUsername() == null || account.getUsername().isBlank()) return null;
        Account actual = accountRepository.findByUsername(account.getUsername());
        if (actual == null) return null;
        return actual.getPassword().equals(account.getPassword()) ? actual : null;
    }
}
