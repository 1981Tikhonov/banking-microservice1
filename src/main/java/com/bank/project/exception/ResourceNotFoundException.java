package com.bank.project.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s не найден(а) с %s : '%s'", resourceName, fieldName, fieldValue));
    }
}
