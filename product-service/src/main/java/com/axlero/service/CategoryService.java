package com.axlero.service;

import com.axlero.dto.CategoryRequestDto;
import com.axlero.dto.CategoryResponseDto;

import java.util.List;

public interface CategoryService {

    CategoryResponseDto createCategory(CategoryRequestDto dto);

    CategoryResponseDto updateCategory(String id, CategoryRequestDto dto);

    void deleteCategory(String id);

    CategoryResponseDto getCategoryById(String id);

    List<CategoryResponseDto> getAllCategory();
}
