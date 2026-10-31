package com.axlero.inventory_service.service.impl;

import com.axlero.inventory_service.dto.InventoryRequest;
import com.axlero.inventory_service.dto.InventoryResponse;
import com.axlero.inventory_service.entity.Inventory;
import com.axlero.inventory_service.exception.InventoryAlreadyExistsException;
import com.axlero.inventory_service.exception.InventoryNotFoundException;
import com.axlero.inventory_service.mapper.InventoryMapper;
import com.axlero.inventory_service.repository.InventoryRepository;
import com.axlero.inventory_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
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
    public InventoryResponse addInventory(InventoryRequest request) {
        if (inventoryRepository.findBySkuCode(request.getSkuCode()).isPresent()) {
            throw new InventoryAlreadyExistsException(
                    "Inventory already exists for SKU: " + request.getSkuCode());
        }

        Inventory entity = inventoryMapper.toEntity(request);
        entity.setId(UUID.randomUUID().toString());
        Inventory saved  = inventoryRepository.save(entity);    // id populated here
        return inventoryMapper.toResponse(saved);
    }
}
