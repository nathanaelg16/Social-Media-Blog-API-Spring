package com.example.exception;

public class DuplicateAccountRegistrationException extends AccountRegistrationException {
    public DuplicateAccountRegistrationException() {
        super("Account with username already exists");
    }
}
