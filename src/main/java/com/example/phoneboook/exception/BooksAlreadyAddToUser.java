package com.example.phoneboook.exception;

public class BooksAlreadyAddToUser extends Exception {
    public BooksAlreadyAddToUser(String message) {
        super(message);
    }
}
