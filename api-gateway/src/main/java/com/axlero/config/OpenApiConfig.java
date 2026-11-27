package com.axlero.config;


import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public GroupedOpenApi userMicroserviceApi() {
        return GroupedOpenApi.builder()
                .group("user-microservice")
                .pathsToMatch("/api/users/**")
                .build();
    }

    @Bean
    public GroupedOpenApi categoryMicroserviceApi() {
        return GroupedOpenApi.builder()
                .group("product-microservice")
                .pathsToMatch("/api/categories/**,/api/products")
                .build();
    }

    @Bean
    public GroupedOpenApi orderMicroserviceApi() {
        return GroupedOpenApi.builder()
                .group("order-microservice")
                .pathsToMatch("/api/order")
                .build();
    }

    @Bean
    public GroupedOpenApi inventoryMicroserviceApi() {
        return GroupedOpenApi.builder()
                .group("inventory-microservice")
                .pathsToMatch("/api/inventory")
                .build();
    }
}


