package com.example.restwebservice.services.impl;

import com.example.restwebservice.dto.CategoryDto;
import com.example.restwebservice.dto.converters.CategoryConverter;
import com.example.restwebservice.entities.Category;
import com.example.restwebservice.repositories.CategoryRepository;
import com.example.restwebservice.services.CategoryService;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
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

    @Override
    public List<CategoryDto> uploadCategoriesFromFile(MultipartFile file) throws IOException {
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            CsvToBean<CategoryDto> csvToBean = new CsvToBeanBuilder<CategoryDto>(reader)
                    .withType(CategoryDto.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withIgnoreQuotations(true)
                    .withSeparator(',')
                    .build();
            List<CategoryDto> categoryDtoList = new ArrayList<>();
            List<Category> categories = new ArrayList<>();
            csvToBean.forEach(categoryDtoList::add);
            for (CategoryDto dto : categoryDtoList) {
                Category c = categoryRepository.createOrUpdateCategory(categoryConverter.fromDto(dto));
                categories.add(c);
            }
            return categories.stream().map(categoryConverter::toDto).toList();
        }
    }

    @Override
    public void downloadCategoriesToFile(List<CategoryDto> categories, HttpServletResponse response)
            throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {
        try (Writer writer = new OutputStreamWriter(response.getOutputStream())) {
            StatefulBeanToCsv<CategoryDto> beanToCsv = new StatefulBeanToCsvBuilder<CategoryDto>(writer)
                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                    .withSeparator(',')
                    .build();
            response.setContentType("text/csv");
            response.setHeader("Content-Disposition", "attachment; filename=" + "categories.csv");
            beanToCsv.write(categories);
        }
    }
}
