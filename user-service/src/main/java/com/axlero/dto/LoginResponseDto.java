package com.axlero.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDto {

    private String token;
    private String tokenType= "Bearer";
    private String customerId;
    private String email;
    private String name;
}
