package com.axlero.inventory_service.consumer;

import com.axlero.inventory_service.event.OrderPlacedEvent;
import com.axlero.inventory_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderEventConsumer {

    private final InventoryService inventoryService;

    @KafkaListener(topics = "order-event", groupId = "inventory-group")
    public void consume(OrderPlacedEvent event) {
        log.info("Received OrderPlacedEvent: eventId={}, orderId={}, skuCode={}, quantity={}",
                event.getEventId(), event.getOrderId(), event.getSkuCode(), event.getQuantity());

        try {
            inventoryService.updateStock(event);
            log.info("Stock updated successfully for orderId: {}", event.getOrderId());
        } catch (Exception e) {
            log.error("Failed to update stock for orderId: {}. Reason: {}",
                    event.getOrderId(), e.getMessage(), e);
        }
    }
}