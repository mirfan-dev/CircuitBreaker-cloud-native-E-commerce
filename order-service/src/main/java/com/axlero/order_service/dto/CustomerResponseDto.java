package com.axlero.order_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponseDto {

    private String id;

    private String name;

    private String email;

    private String phone;

    private String roles;
}
