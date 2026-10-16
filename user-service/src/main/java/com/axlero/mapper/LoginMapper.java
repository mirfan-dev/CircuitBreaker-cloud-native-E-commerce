package com.axlero.mapper;

import com.axlero.dto.LoginResponseDto;
import com.axlero.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LoginMapper {

    @Mapping(target = "token", source = "token")
    @Mapping(target = "tokenType", constant = "Bearer")
    @Mapping(target = "customerId", source = "customer.id")
    LoginResponseDto toLoginResponse(Customer customer, String token);
}