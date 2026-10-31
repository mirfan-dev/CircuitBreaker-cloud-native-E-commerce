package com.axlero.inventory_service.mapper;

import com.axlero.inventory_service.dto.InventoryRequest;
import com.axlero.inventory_service.dto.InventoryResponse;
import com.axlero.inventory_service.entity.Inventory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface InventoryMapper {

    @Mapping(target = "inStock", source = "quantity", qualifiedByName = "toInStock")
    @Mapping(target = "availableQuantity", source = "quantity")
    InventoryResponse toResponse(Inventory inventory);

    @Mapping(target = "id", ignore = true)
    Inventory toEntity(InventoryRequest request);


    @Named("toInStock")
    default boolean toInStock(Integer quantity) {
        return quantity != null && quantity > 0;
    }
}