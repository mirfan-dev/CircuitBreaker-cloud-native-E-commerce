package com.axlero.service;

import com.axlero.dto.ProductRequestDto;
import com.axlero.dto.ProductResponseDto;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {

        ProductResponseDto createProduct(ProductRequestDto dto);

        ProductResponseDto updateProduct(String id, ProductRequestDto dto);

        void deleteProduct(String id);

        ProductResponseDto getProductById(String id);

        Page<ProductResponseDto> getAllProduct(int page, int size, String sortBy, String sortDir);

        Page<ProductResponseDto> searchProduct(String keyword, int page, int size);

        Page<ProductResponseDto> filterProducts(String categoryId,
                                                Double minPrice,
                                                Double maxPrice,
                                                int page,
                                                int size);

        Page<ProductResponseDto> advanceFilter(String keyword,
                                               String categoryId,
                                               Double minPrice,
                                               Double maxPrice,
                                               int page,
                                               int size,
                                               String sortBy,
                                               String sortDir);

        ProductResponseDto uploadImage(String productId, MultipartFile file) throws IOException;

        public Resource serveFile(@PathVariable String id) throws IOException;



}
