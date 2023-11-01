package com.example.restwebservice.services;

import com.example.restwebservice.dto.ProductDto;

import java.util.List;

public interface ProductService {

    List<ProductDto> getAllProducts();

    List<ProductDto> searchProducts(String search);

    List<ProductDto> getProductByCategoryId(int id);

    ProductDto createProduct(ProductDto productDto);

    void deleteProduct(int id);

    ProductDto updateProduct(ProductDto productDto);

    ProductDto getProductById(int id);
}
