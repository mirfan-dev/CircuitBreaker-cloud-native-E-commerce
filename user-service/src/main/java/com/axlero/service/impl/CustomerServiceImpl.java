package com.axlero.service.impl;

import com.axlero.dto.CustomerRequestDto;
import com.axlero.dto.CustomerResponseDto;
import com.axlero.dto.LoginRequestDto;
import com.axlero.dto.LoginResponseDto;
import com.axlero.entity.Customer;
import com.axlero.exception.ResourceAlreadyExistException;
import com.axlero.exception.ResourceNotFoundException;
import com.axlero.mapper.CustomerMapper;
import com.axlero.mapper.LoginMapper;
import com.axlero.repository.CustomerRepository;
import com.axlero.security.JwtToken;
import com.axlero.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtToken jwtToken;
    private final LoginMapper loginMapper;

    @Override
    public CustomerResponseDto createCustomer(CustomerRequestDto request) {

        if(customerRepository.existsByEmail(request.getEmail())){
            throw new ResourceAlreadyExistException("Email already exist");
        }
        Customer customer = customerMapper.toEntity(request);
        customer.setId(UUID.randomUUID().toString());
        customer.setPassword(passwordEncoder.encode(request.getPassword()));
        Customer saved = customerRepository.save(customer);
        return customerMapper.toResponse(saved);
    }

    @Override
    public LoginResponseDto login(LoginRequestDto dto) {
        Customer customer= customerRepository.findByEmail(dto.getEmail())
                .orElseThrow(()-> new ResourceNotFoundException("Invalid credential"));
        boolean matches = passwordEncoder.matches(dto.getPassword(), customer.getPassword());
        if (!matches){
            throw new ResourceNotFoundException("Invalid credentials");

        }
        String token = jwtToken.generateToken(customer.getId(),customer.getEmail(),customer.getRoles());
        return loginMapper.toLoginResponse(customer,token);
    }

    @Override
    public CustomerResponseDto getCustomerById(String id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + id));
        return customerMapper.toResponse(customer);
    }

    @Override
    public List<CustomerResponseDto> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(customerMapper::toResponse)
                .toList();
    }

    @Override
    public CustomerResponseDto updateCustomer(String id, CustomerRequestDto request) {
        Customer existing = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + id));

        existing.setName(request.getName());
        existing.setEmail(request.getEmail());
        existing.setPhone(request.getPhone());

        // Only re-hash if the password was actually changed
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            existing.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        Customer updated = customerRepository.save(existing);
        return customerMapper.toResponse(updated);
    }

    @Override
    public void deleteCustomer(String id) {
        if (!customerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Customer not found: " + id);
        }
        customerRepository.deleteById(id);
    }
}