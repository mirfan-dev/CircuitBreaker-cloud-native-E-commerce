package com.axlero.order_service.service;

import com.axlero.order_service.dto.OrderRequest;
import com.axlero.order_service.dto.OrderResponse;

public interface OrderService {

    public OrderResponse placedOrder(OrderRequest request);

}
