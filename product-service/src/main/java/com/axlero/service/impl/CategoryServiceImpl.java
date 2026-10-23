package com.axlero.service.impl;

import com.axlero.dto.CategoryRequestDto;
import com.axlero.dto.CategoryResponseDto;
import com.axlero.entity.Category;
import com.axlero.exception.ResourceNotFoundException;
import com.axlero.mapper.CategoryMapper;
import com.axlero.repository.CategoryRepository;
import com.axlero.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    // ------------------------------------------------------------------ CREATE
    @Override
    @Transactional
    public CategoryResponseDto createCategory(CategoryRequestDto dto) {

        // Guard against duplicate names (assumes Category.name is unique).
        if (categoryRepository.existsByName(dto.getName())) {
            throw new IllegalArgumentException(
                    "Category already exists with name: " + dto.getName());
        }

        Category category = categoryMapper.toEntity(dto);
        category.setId(UUID.randomUUID().toString());
        Category saved = categoryRepository.save(category);
        return categoryMapper.toResponseDto(saved);
    }

    // ------------------------------------------------------------------ UPDATE
    @Override
    @Transactional
    public CategoryResponseDto updateCategory(String id, CategoryRequestDto dto) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category not found: " + id));

        // If the name is being changed, ensure it's not taken by another category.
        if (!category.getName().equals(dto.getName())
                && categoryRepository.existsByName(dto.getName())) {
            throw new IllegalArgumentException(
                    "Category already exists with name: " + dto.getName());
        }

        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        category.setParentId(dto.getParentId());

        Category saved = categoryRepository.save(category);
        return categoryMapper.toResponseDto(saved);
    }

    // ------------------------------------------------------------------ DELETE
    @Override
    @Transactional
    public void deleteCategory(String id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category not found: " + id));

        categoryRepository.delete(category);
    }

    // ------------------------------------------------------------------ GET ONE
    @Override
    public CategoryResponseDto getCategoryById(String id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category not found: " + id));
        return categoryMapper.toResponseDto(category);
    }

    // ------------------------------------------------------------------ GET ALL
    @Override
    public List<CategoryResponseDto> getAllCategory() {
        return categoryRepository.findAll(Sort.by("name").ascending())
                .stream()
                .map(categoryMapper::toResponseDto)
                .toList();
    }
}