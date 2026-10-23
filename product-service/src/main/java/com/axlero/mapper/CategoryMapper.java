package com.axlero.mapper;

import com.axlero.dto.CategoryRequestDto;
import com.axlero.dto.CategoryResponseDto;
import com.axlero.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {


    @Mapping(target = "id",       ignore = true)   // server-generated
    @Mapping(target = "parentId", source = "parentId")   // String -> String
    @Mapping(target = "products", ignore = true)   // association, not part of DTO
    Category toEntity(CategoryRequestDto dto);


    @Mapping(target = "parentId", source = "parentId")
    CategoryResponseDto toResponseDto(Category entity);
}