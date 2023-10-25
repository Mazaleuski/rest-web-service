package com.example.restwebservice.services.impl;

import com.example.restwebservice.dto.CategoryDto;
import com.example.restwebservice.dto.converters.CategoryConverter;
import com.example.restwebservice.entities.Category;
import com.example.restwebservice.repositories.CategoryRepository;
import com.example.restwebservice.services.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryConverter categoryConverter;

    @Override
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream().map(categoryConverter::toDto).toList();
    }

    @Override
    public CategoryDto getCategoryById(int id) {
        Category category = Optional.ofNullable(categoryRepository.findById(id))
                .orElseThrow(() -> new EntityNotFoundException(String.format("Category with id %d not found", id)));
        return categoryConverter.toDto(category);
    }

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category category = categoryConverter.fromDto(categoryDto);
        category = categoryRepository.createOrUpdateCategory(category);
        return categoryConverter.toDto(category);
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto) {
        Category category = Optional.ofNullable(categoryRepository.findById(categoryDto.getId()))
                .orElseThrow(() -> new EntityNotFoundException(String.format("Category with id %d not found", categoryDto.getId())));
        category.setName(categoryDto.getName());
        category.setRating(categoryDto.getRating());
        return categoryConverter.toDto(categoryRepository.createOrUpdateCategory(category));
    }

    @Override
    public void deleteCategory(int id) {
        categoryRepository.delete(id);
    }
}
