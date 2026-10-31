package com.axlero.inventory_service.service;

import com.axlero.inventory_service.dto.InventoryRequest;
import com.axlero.inventory_service.dto.InventoryResponse;

public interface InventoryService {

    public InventoryResponse checkInventory(String skuCode);

    public InventoryResponse addInventory(InventoryRequest inventoryRequest);
}
