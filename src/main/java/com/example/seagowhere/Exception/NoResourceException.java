package com.example.seagowhere.Exception;

public class NoResourceException extends RuntimeException {

    public NoResourceException() {
        super("Requested resource was not found.");
    }
}

