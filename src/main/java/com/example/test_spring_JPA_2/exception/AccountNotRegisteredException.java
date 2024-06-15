package com.example.test_spring_JPA_2.exception;

public class AccountNotRegisteredException extends RuntimeException {
    public AccountNotRegisteredException(String message) {
        super(message);
    }
}