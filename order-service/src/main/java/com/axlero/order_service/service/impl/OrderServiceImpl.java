
package com.axlero.order_service.service.impl;

import com.axlero.order_service.dto.CustomerResponseDto;
import com.axlero.order_service.dto.OrderRequest;
import com.axlero.order_service.dto.OrderResponse;
import com.axlero.order_service.entity.Order;
import com.axlero.order_service.enums.OrderStatus;
import com.axlero.order_service.event.OrderPlacedEvent;
import com.axlero.order_service.kafka.OrderEventProducer;
import com.axlero.order_service.mapper.OrderMapper;
import com.axlero.order_service.repository.OrderRepository;
import com.axlero.order_service.service.OrderService;
import com.axlero.order_service.service.extend.CustomerService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderEventProducer orderEventProducer;
    private final CustomerService customerService;

    @Override
    @Transactional
    public OrderResponse placedOrder(OrderRequest request) {


        // Validate customer through User Service
        CustomerResponseDto customerResponseDto =
                customerService.getById(request.getCustomerId());


        // Convert request to entity
        Order order = orderMapper.toEntity(request);

        // Set order details
        order.setId(UUID.randomUUID().toString());
        order.setOrderNumber("ORD-" + UUID.randomUUID());
        order.setOrderStatus(OrderStatus.PENDING);

        // Save order
        Order savedOrder = orderRepository.save(order);

        // Create Kafka event
        OrderPlacedEvent event = new OrderPlacedEvent();

        event.setEventId(UUID.randomUUID().toString());
        event.setOrderId(savedOrder.getId());
        event.setSkuCode(savedOrder.getSkuCode());
        event.setQuantity(savedOrder.getQuantity());
        event.setEventTime(LocalDateTime.now());
        orderEventProducer.sendOrderEvent(event);

        // Convert entity to response
        OrderResponse response = orderMapper.toResponse(savedOrder);

        // Attach customer details
        response.setCustomerResponseDto(customerResponseDto);


        return response;
    }
}
