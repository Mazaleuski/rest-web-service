package com.example.restwebservice.dto.converters;

import com.example.restwebservice.dto.ProductDto;
import com.example.restwebservice.entities.Product;
import com.example.restwebservice.repositories.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class ProductConverter {
    private final CategoryRepository categoryRepository;

    public ProductDto toDto(Product product) {
        return Optional.ofNullable(product).map(p -> ProductDto.builder()
                        .id(p.getId())
                        .name(p.getName())
                        .description(p.getDescription())
                        .price(p.getPrice())
                        .categoryId(p.getCategory().getId())
                        .imagePath(p.getImagePath())
                        .build())
                .orElse(null);
    }

    public Product fromDto(ProductDto productDto) {
        return Optional.ofNullable(productDto).map(p -> Product.builder()
                        .id(p.getId())
                        .name(p.getName())
                        .description(p.getDescription())
                        .price(p.getPrice())
                        .category(categoryRepository.findById(p.getCategoryId()).orElse(null))
                        .imagePath(p.getImagePath())
                        .build())
                .orElse(null);
    }
}
