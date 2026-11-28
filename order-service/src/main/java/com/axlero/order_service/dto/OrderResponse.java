package com.axlero.order_service.dto;

import com.axlero.order_service.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {

    private String id;

    private String orderNumber;

    private OrderStatus orderStatus;

    private String customerId;

    private CustomerResponseDto customerResponseDto;


}
