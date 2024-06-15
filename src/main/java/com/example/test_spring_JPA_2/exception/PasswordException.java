package com.example.test_spring_JPA_2.exception;

public class PasswordException extends RuntimeException {
    public PasswordException(String message) {
        super(message);
    }
}