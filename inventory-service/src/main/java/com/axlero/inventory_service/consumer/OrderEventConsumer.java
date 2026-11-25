package com.axlero.inventory_service.consumer;

import com.axlero.inventory_service.event.OrderPlacedEvent;
import com.axlero.inventory_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderEventConsumer {

    private final InventoryService inventoryService;

    @KafkaListener(topics = "order-event",groupId = "inventory-group")
    public void consume(OrderPlacedEvent event){

    }
}
