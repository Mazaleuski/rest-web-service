package com.example.restwebservice.dto.converters;

import com.example.restwebservice.dto.CategoryDto;
import com.example.restwebservice.entities.Category;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class CategoryConverter {
    private final ProductConverter productConverter;

    public CategoryDto toDto(Category category) {
        return Optional.ofNullable(category).map(c -> CategoryDto.builder()
                        .id(c.getId())
                        .name(c.getName())
                        .rating(c.getRating())
                        .imagePath(c.getImagePath())
                        .products(Optional.ofNullable(c.getProductList()).map(products -> products.stream()
                                .map(productConverter::toDto).toList()).orElse(List.of()))
                        .build())
                .orElse(null);
    }

    public Category fromDto(CategoryDto categoryDto) {
        return Optional.ofNullable(categoryDto).map(c -> Category.builder()
                        .id(c.getId())
                        .name(c.getName())
                        .rating(c.getRating())
                        .imagePath(c.getImagePath())
                        .productList(Optional.ofNullable(c.getProducts()).map(products -> products.stream()
                                .map(productConverter::fromDto).toList()).orElse(List.of()))
                        .build())
                .orElse(null);
    }
}
