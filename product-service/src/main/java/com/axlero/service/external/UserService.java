package com.axlero.service.external;

import com.axlero.config.AppConstants;
import com.axlero.dto.CustomerResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final WebClient.Builder webClientBuilder;

    public CustomerResponseDto getById(String restId) {
        return webClientBuilder.build()
                .get()
                .uri("http://USER-SERVICE/api/users/{id}", restId)   // ← /api/users
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
