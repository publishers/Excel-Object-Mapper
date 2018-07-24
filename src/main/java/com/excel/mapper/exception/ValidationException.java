package com.excel.mapper.exception;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(Exception ex, String message) {
        super(message, ex);
    }
}
