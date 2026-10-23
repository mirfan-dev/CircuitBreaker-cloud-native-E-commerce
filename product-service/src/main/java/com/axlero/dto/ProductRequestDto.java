package com.axlero.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDto {

    private String id;

    @NotBlank(message = "Product name is required")
    private String name;

    private String description;

    @Positive(message = "Price must be positive ")
    private Double price;

    private Double discountPrice;

    @Min(value = 0, message = "Quantity must be 0 or more ")
    private Integer quantity;

    private String brand;

    private String imageUrl;

    private String categoryId;
}
