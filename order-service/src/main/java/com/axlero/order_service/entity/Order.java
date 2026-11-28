package com.axlero.order_service.entity;

import com.axlero.order_service.enums.OrderStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "/orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    private String id;

    private String customerId;

    private String orderNumber;

    private String skuCode;

    private Integer quantity;

    private BigDecimal totalAmount;

    private OrderStatus orderStatus;
}
