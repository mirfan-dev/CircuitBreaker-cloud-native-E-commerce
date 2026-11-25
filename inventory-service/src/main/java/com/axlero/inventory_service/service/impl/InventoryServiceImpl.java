package com.axlero.inventory_service.service.impl;

import com.axlero.inventory_service.dto.InventoryRequest;
import com.axlero.inventory_service.dto.InventoryResponse;
import com.axlero.inventory_service.entity.Inventory;
import com.axlero.inventory_service.event.OrderPlacedEvent;
import com.axlero.inventory_service.exception.InventoryAlreadyExistsException;
import com.axlero.inventory_service.exception.InventoryNotFoundException;
import com.axlero.inventory_service.mapper.InventoryMapper;
import com.axlero.inventory_service.repository.InventoryRepository;
import com.axlero.inventory_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryMapper inventoryMapper;

    @Override
    public InventoryResponse checkInventory(String skuCode) {
        Inventory inventory = inventoryRepository.findBySkuCode(skuCode)
                .orElseThrow(() -> new InventoryNotFoundException("Inventory not found for SKU: " + skuCode));

        return inventoryMapper.toResponse(inventory);
    }

    @Override
    @Transactional
    public void addInventory(InventoryRequest request) {
        if (inventoryRepository.findBySkuCode(request.getSkuCode()).isPresent()) {
            throw new InventoryAlreadyExistsException(
                    "Inventory already exists for SKU: " + request.getSkuCode());
        }

        Inventory entity = inventoryMapper.toEntity(request);
        entity.setId(UUID.randomUUID().toString());
        inventoryRepository.save(entity);
    }

    public void updateStock(OrderPlacedEvent event){

        Inventory inventory = inventoryRepository.findBySkuCode(event.getSkuCode())
                .orElseThrow(() -> new InventoryAlreadyExistsException(
                    "Inventory not found with skuCode: " + event.getSkuCode()));

        Integer availableQuantity = inventory.getQuantity();
        Integer orderedQuantity = event.getQuantity();

        if (availableQuantity < orderedQuantity){
            throw new ResourceNotFoundException("Insufficient stock for this skuCode");
        }
        inventory.setQuantity(availableQuantity- orderedQuantity);
        inventoryRepository.save(inventory);
    }
}
