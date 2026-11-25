package com.axlero.order_service.service.impl;

import com.axlero.order_service.dto.OrderRequest;
import com.axlero.order_service.dto.OrderResponse;
import com.axlero.order_service.entity.Order;
import com.axlero.order_service.event.OrderPlacedEvent;
import com.axlero.order_service.kafka.OrderEventProducer;
import com.axlero.order_service.mapper.OrderMapper;
import com.axlero.order_service.repository.OrderRepository;
import com.axlero.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderEventProducer orderEventProducer;

    @Override
    public OrderResponse placedOrder(OrderRequest request) {

        Order order = orderMapper.toEntity(request);
        order.setId(UUID.randomUUID().toString());
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setOrderStatus("CREATED");
        orderRepository.save(order);
        OrderPlacedEvent orderPlacedEvent = new OrderPlacedEvent();
        orderPlacedEvent.setEventId(UUID.randomUUID().toString());
        orderPlacedEvent.setOrderId(order.getId());
        orderPlacedEvent.setQuantity(request.getQuantity());
        orderPlacedEvent.setSkuCode(request.getSkuCode());
        orderPlacedEvent.setEventTime(LocalDateTime.now());
        orderEventProducer.sendOrderEvent(orderPlacedEvent);
        return orderMapper.toResponse(order);
    }
}
