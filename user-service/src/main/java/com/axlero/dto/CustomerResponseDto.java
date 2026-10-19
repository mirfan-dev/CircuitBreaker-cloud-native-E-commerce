package com.axlero.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponseDto {

    private String id;

    private String name;

    private String email;

    private String phone;

    private String roles;

    private String password;

    private Instant createdAt;

    private Instant updatedAt;
}