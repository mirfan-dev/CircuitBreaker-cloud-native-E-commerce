package com.axlero.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductResponseDto {


    private String id;


    private String name;


    private String description;


    private Double price;

    private Double discountPrice;

    private Double discountPercentage;

    private Integer quantity;

    private String brand;

    private String imageUrl;

    private String categoryId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
