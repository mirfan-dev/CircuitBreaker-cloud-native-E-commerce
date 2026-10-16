package com.axlero.exception;

public class ResourceAlreadyExistException extends RuntimeException{

    public ResourceAlreadyExistException(String message) {
        super(message);
    }

    public ResourceAlreadyExistException() {
    }
}
