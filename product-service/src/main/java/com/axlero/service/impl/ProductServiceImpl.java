package com.axlero.service.impl;

import com.axlero.dto.ProductRequestDto;
import com.axlero.dto.ProductResponseDto;
import com.axlero.entity.Category;
import com.axlero.entity.Product;
import com.axlero.exception.ResourceNotFoundException;
import com.axlero.mapper.ProductMapper;
import com.axlero.payload.FileData;
import com.axlero.repository.ProductRepository;
import com.axlero.service.FileService;
import com.axlero.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final FileService fileService;

    @Value("${app.file.banner-path}")
    private String bannerFolderPath;

    //  CREATE
    @Override
    @Transactional
    public ProductResponseDto createProduct(ProductRequestDto dto) {

        validateDiscount(dto);

        Category category = null;
        if(dto.getCategoryId() != null){
            category = new Category();
            category.setId(dto.getCategoryId());
        }

        Product product = productMapper.toEntity(dto);
        product.setId(UUID.randomUUID().toString());
        product.setCategory(category);

        Product saved = productRepository.save(product);
        return productMapper.toResponseDto(saved);
    }

    //  UPDATE
    @Override
    @Transactional
    public ProductResponseDto updateProduct(String id, ProductRequestDto dto) {

        validateDiscount(dto);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));

        Category category = null;
        if(dto.getCategoryId() != null){
            category = new Category();
            category.setId(dto.getCategoryId());
            product.setCategory(category);
        }

        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setDiscountPrice(dto.getDiscountPrice());

        Product saved = productRepository.save(product);
        return productMapper.toResponseDto(saved);
    }

    // ------------------------------------------------------------------ DELETE
    @Override
    @Transactional
    public void deleteProduct(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));
        productRepository.delete(product);
    }

    // ------------------------------------------------------------------ GET ONE
    @Override
    public ProductResponseDto getProductById(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));
        return productMapper.toResponseDto(product);
    }

    //  GET ALL
    @Override
    public Page<ProductResponseDto> getAllProduct(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc")? Sort.by(sortBy).ascending(): Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return productRepository.findAll(pageable)
                .map(productMapper::toResponseDto);
    }

    // SEARCH
    @Override
    public Page<ProductResponseDto> searchProduct(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.searchProduct(keyword, pageable)
                .map(productMapper::toResponseDto);
    }

    // ------------------------------------------------------------------ FILTER
    @Override
    public Page<ProductResponseDto> filterProducts(String categoryId,
                                                   Double minPrice,
                                                   Double maxPrice,
                                                   int page,
                                                   int size) {
        Pageable pageable = PageRequest.of(page, size);

        // Delegate to the null-safe advanceFilter query.
        return productRepository.advanceFilter(null, categoryId, minPrice, maxPrice, pageable)
                .map(productMapper::toResponseDto);
    }

    // ADVANCED FILTER
    @Override
    public Page<ProductResponseDto> advanceFilter(String keyword,
                                                  String categoryId,
                                                  Double minPrice,
                                                  Double maxPrice,
                                                  int page,
                                                  int size,
                                                  String sortBy,
                                                  String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc")? Sort.by(sortBy).ascending(): Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        // Normalize blank keyword -> null so the JPQL "IS NULL" branch works.
        String kw = (keyword == null || keyword.isBlank()) ? null : keyword;

        return productRepository.advanceFilter(kw, categoryId, minPrice, maxPrice, pageable)
                .map(productMapper::toResponseDto);
    }

    @Override
    @Transactional
    public ProductResponseDto uploadImage(String productId, MultipartFile file) throws IOException {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " +productId));
        String fileName=file.getOriginalFilename();

        String fileExtension=fileName.substring(fileName.lastIndexOf("."));

        String newFileName=new Date().getTime()+fileExtension;

        FileData fileData=fileService.uploadFile(file, bannerFolderPath + newFileName);

        product.setImageUrl(bannerFolderPath + fileData.fileName());
        productRepository.save(product);

        return productMapper.toResponseDto(product);
    }

    @Override
    public Resource serveFile(String id) throws IOException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " +id));

        if (product.getImageUrl()== null || product.getImageUrl().isBlank()) {
            throw new ResourceNotFoundException("Product has no image");
        }

        Path filePath = Paths.get(product.getImageUrl());

        Resource resource = new UrlResource(( filePath).toUri());

        if (resource.exists()) {
            return resource;
        } else {
            throw new ResourceNotFoundException("File Not found");
        }
    }


    private void validateDiscount(ProductRequestDto dto) {
        if (dto.getDiscountPrice() != null
                && dto.getPrice() != null
                && dto.getDiscountPrice() > dto.getPrice()) {
            throw new IllegalArgumentException("Discount price cannot exceed price");
        }
    }
}