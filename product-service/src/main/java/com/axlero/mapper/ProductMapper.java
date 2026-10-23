package com.axlero.mapper;

import com.axlero.dto.ProductRequestDto;
import com.axlero.dto.ProductResponseDto;
import com.axlero.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {


    @Mapping(target = "id",        ignore = true)
    @Mapping(target = "category",  ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Product toEntity(ProductRequestDto dto);

    @Mapping(target = "categoryId",   source = "category.id")
    ProductResponseDto toResponseDto(Product entity);
}