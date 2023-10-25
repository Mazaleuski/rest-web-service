package com.example.restwebservice.exceptions;

public class CartIsEmptyException extends Exception {
    public CartIsEmptyException(String message) {
        super(message);
    }
}
