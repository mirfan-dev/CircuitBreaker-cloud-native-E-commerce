package com.axlero.controller;

import com.axlero.dto.ProductRequestDto;
import com.axlero.dto.ProductResponseDto;
import com.axlero.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponseDto create(@Valid @RequestBody ProductRequestDto dto) {
        return productService.createProduct(dto);
    }

    @PutMapping("/{id}")
    public ProductResponseDto update(@PathVariable String id,
                                     @Valid @RequestBody ProductRequestDto dto) {
        return productService.updateProduct(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        productService.deleteProduct(id);
    }

    @GetMapping("/{id}")
    public ProductResponseDto get(@PathVariable String id) {
        return productService.getProductById(id);
    }

    @GetMapping
    public Page<ProductResponseDto> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return productService.getAllProduct(page, size, sortBy, sortDir);
    }

    @GetMapping("/search")
    public Page<ProductResponseDto> search(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return productService.searchProduct(keyword, page, size);
    }

    @GetMapping("/filter")
    public Page<ProductResponseDto> filter(
            @RequestParam(required = false) String categoryId,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return productService.filterProducts(categoryId, minPrice, maxPrice, page, size);
    }

    @GetMapping("/advanced-filter")
    public Page<ProductResponseDto> advancedFilter(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String categoryId,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return productService.advanceFilter(keyword, categoryId, minPrice, maxPrice,
                page, size, sortBy, sortDir);
    }

    @PostMapping(value = "/upload-image/{productId}")
    public ResponseEntity<ProductResponseDto> uploadImage(
            @PathVariable("productId") String productId,
            @RequestParam("file") MultipartFile file) throws IOException {

        ProductResponseDto response = productService.uploadImage(productId, file);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/banner")
    public ResponseEntity<Resource> serveFile(@PathVariable String id) throws IOException {

        Resource resource = productService.serveFile(id);

        String contentType = Files.probeContentType(Paths.get(resource.getURI()));
        MediaType mediaType = (contentType != null)
                ? MediaType.parseMediaType(contentType)
                : MediaType.APPLICATION_OCTET_STREAM;

        return ResponseEntity.ok()
                .contentType(mediaType)
                .body(resource);
    }
}
