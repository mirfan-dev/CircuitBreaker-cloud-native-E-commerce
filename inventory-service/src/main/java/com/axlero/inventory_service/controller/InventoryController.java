package com.axlero.inventory_service.controller;


import com.axlero.inventory_service.dto.InventoryRequest;
import com.axlero.inventory_service.dto.InventoryResponse;
import com.axlero.inventory_service.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/{skuCode}")
    public ResponseEntity<InventoryResponse> checkInventory(@PathVariable String skuCode) {
        return ResponseEntity.ok(inventoryService.checkInventory(skuCode));
    }

    @PostMapping
    public ResponseEntity<InventoryResponse> addInventory(@Valid @RequestBody InventoryRequest request) {
        InventoryResponse response = inventoryService.addInventory(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
