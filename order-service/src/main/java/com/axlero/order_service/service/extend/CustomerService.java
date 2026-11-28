package com.axlero.order_service.service.extend;

import com.axlero.order_service.dto.CustomerResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final WebClient.Builder webClientBuilder;

    public CustomerResponseDto getById(String userId) {

        return webClientBuilder.build()
                .get()
                .uri("http://USER-SERVICE/api/users/{id}", userId)
                .retrieve()
                .bodyToMono(CustomerResponseDto.class)
                .block();
    }

    public List<CustomerResponseDto> getAll() {
        return webClientBuilder.build()
                .get()
                .uri("http://USER-SERVICE/api/users")                // ← /api/users
                .retrieve()
                .bodyToFlux(CustomerResponseDto.class)
                .collectList()
                .block();
    }
}
