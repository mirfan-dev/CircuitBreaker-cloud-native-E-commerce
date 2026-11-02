package com.axlero.order_service.mapper;

import com.axlero.order_service.dto.OrderRequest;
import com.axlero.order_service.dto.OrderResponse;
import com.axlero.order_service.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderNumber", ignore = true)
    @Mapping(target = "orderStatus", ignore = true)
    Order toEntity(OrderRequest orderRequest);

    OrderResponse toResponse(Order order);
}