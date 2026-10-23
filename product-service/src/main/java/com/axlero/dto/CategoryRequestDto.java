package com.axlero.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryRequestDto {

    private String id;

    @NotBlank(message = "Category name can't be empty")
    private String name;

    private String description;

    private String parentId;
}
