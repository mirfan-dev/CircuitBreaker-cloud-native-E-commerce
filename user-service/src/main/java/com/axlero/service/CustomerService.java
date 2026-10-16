package com.axlero.service;



import com.axlero.dto.CustomerRequestDto;
import com.axlero.dto.CustomerResponseDto;
import com.axlero.dto.LoginRequestDto;
import com.axlero.dto.LoginResponseDto;

import java.util.List;

public interface CustomerService {
    CustomerResponseDto createCustomer(CustomerRequestDto request);
    LoginResponseDto login(LoginRequestDto dto);
    CustomerResponseDto getCustomerById(String  id);
    List<CustomerResponseDto> getAllCustomers();
    CustomerResponseDto updateCustomer(String id, CustomerRequestDto request);
    void deleteCustomer(String id);

}
