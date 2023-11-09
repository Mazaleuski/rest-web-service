package com.example.restwebservice.services;

import com.example.restwebservice.dto.CategoryDto;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CategoryService {

    List<CategoryDto> getAllCategories();

    CategoryDto getCategoryById(int id);

    CategoryDto createCategory(CategoryDto categoryDto);

    CategoryDto updateCategory(CategoryDto categoryDto);

    void deleteCategory(int id);

    List<CategoryDto> uploadCategoriesFromFile(MultipartFile file) throws IOException;

    void downloadCategoriesToFile(List<CategoryDto> categories, String path)
            throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException;
}
