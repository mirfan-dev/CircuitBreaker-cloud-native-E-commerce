package com.axlero.inventory_service.service;

import com.axlero.inventory_service.dto.InventoryRequest;
import com.axlero.inventory_service.dto.InventoryResponse;
import com.axlero.inventory_service.event.OrderPlacedEvent;

public interface InventoryService {

    public InventoryResponse checkInventory(String skuCode);

    public void addInventory(InventoryRequest inventoryRequest);

    public void updateStock(OrderPlacedEvent event);
}
