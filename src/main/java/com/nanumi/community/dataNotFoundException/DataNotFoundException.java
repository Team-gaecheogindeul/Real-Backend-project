package com.nanumi.community.dataNotFoundException;

public class DataNotFoundException extends RuntimeException {
    public DataNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}