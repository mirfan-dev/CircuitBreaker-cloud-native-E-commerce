package com.axlero.inventory_service.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryResponse {
    private String id;
    private String skuCode;
    private boolean inStock;
    private Integer availableQuantity;
}
