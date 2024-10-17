package com.example.seagowhere.Exception;

public class PasswordBlankException extends RuntimeException {

    public PasswordBlankException() {
        super("Password cannot be blank.");
    }
}

