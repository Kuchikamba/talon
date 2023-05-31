package com.example.phoneboook.exception;

import java.util.function.Supplier;

public class UserNotFoundException extends Exception implements Supplier {
    public UserNotFoundException(String message) {
        super(message);
    }

    @Override
    public Exception get() {
        return null;
    }
}

