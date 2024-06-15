package com.example.test_spring_JPA_2.exception;

public class UsernameException extends RuntimeException {
    public UsernameException(String message) {
        super(message);
    }
}