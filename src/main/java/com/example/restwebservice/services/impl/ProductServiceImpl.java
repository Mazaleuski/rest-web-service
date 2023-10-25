package com.example.restwebservice.services.impl;

import com.example.restwebservice.dto.ProductDto;
import com.example.restwebservice.dto.converters.ProductConverter;
import com.example.restwebservice.entities.Product;
import com.example.restwebservice.repositories.CategoryRepository;
import com.example.restwebservice.repositories.ProductRepository;
import com.example.restwebservice.services.ProductService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductConverter productConverter;
    private final CategoryRepository categoryRepository;

    @Override
    public List<ProductDto> getAllProducts() {
        return productRepository.findAll().stream().map(productConverter::toDto).toList();
    }

    @Override
    public List<ProductDto> searchProducts(String search) {
        return productRepository.findByNameOrDescription(search).stream().map(productConverter::toDto).toList();
    }

    @Override
    public List<ProductDto> getProductByCategoryId(int id) {
        if (categoryRepository.findById(id) == null) {
            throw new EntityNotFoundException(String.format("Category with id %d not found", id));
        }
        List<ProductDto> list = productRepository.findByCategoryId(id).stream().map(productConverter::toDto).toList();
        if (list.size() == 0) {
            throw new EntityNotFoundException(String.format("Category with id %d dont have products", id));
        }
        return list;
    }

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        Product product = productConverter.fromDto(productDto);
        product = productRepository.createOrUpdateProduct(product);
        return productConverter.toDto(product);
    }

    @Override
    public void deleteProduct(int id) {
        productRepository.delete(id);
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto) {
        Product product = Optional.ofNullable(productRepository.findById(productDto.getId()))
                .orElseThrow(() -> new EntityNotFoundException(String.format("Product with id %d not found", productDto.getId())));
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        return productConverter.toDto(productRepository.createOrUpdateProduct(product));
    }

    @Override
    public ProductDto getProductById(int id) {
        Product product = Optional.ofNullable(productRepository.findById(id))
                .orElseThrow(() -> new EntityNotFoundException(String.format("Product with id %d not found", id)));
        return productConverter.toDto(product);
    }
}
