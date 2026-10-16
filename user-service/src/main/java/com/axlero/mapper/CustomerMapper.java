package com.axlero.mapper;

import com.axlero.dto.CustomerRequestDto;
import com.axlero.dto.CustomerResponseDto;
import com.axlero.entity.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    Customer toEntity(CustomerRequestDto request);

    CustomerResponseDto toResponse(Customer customer);
}