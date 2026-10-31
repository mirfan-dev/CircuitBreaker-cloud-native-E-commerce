package com.axlero.inventory_service.exception;

public class InventoryAlreadyExistsException extends RuntimeException {

    public InventoryAlreadyExistsException() {
    }

    public InventoryAlreadyExistsException(String message) {
        super(message);
    }
}
