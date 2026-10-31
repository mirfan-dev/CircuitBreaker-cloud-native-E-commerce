package com.axlero.inventory_service.exception;

public class InventoryNotFoundException extends RuntimeException{

    public InventoryNotFoundException() {
    }

    public InventoryNotFoundException(String message) {
        super(message);
    }
}
