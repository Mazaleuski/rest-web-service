package com.example.restwebservice.services.impl;

import com.example.restwebservice.dto.ProductDto;
import com.example.restwebservice.dto.SearchParamsDto;
import com.example.restwebservice.dto.converters.ProductConverter;
import com.example.restwebservice.entities.Product;
import com.example.restwebservice.repositories.CategoryRepository;
import com.example.restwebservice.repositories.ProductRepository;
import com.example.restwebservice.repositories.ProductSearchSpecification;
import com.example.restwebservice.services.ProductService;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductConverter productConverter;
    private final CategoryRepository categoryRepository;

    @Override
    public List<ProductDto> getAllProducts(int pageNumber, int pageSize) {
        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by("id").ascending());
        return productRepository.findAll(paging).stream().map(productConverter::toDto).toList();
    }

    @Override
    public List<ProductDto> searchProducts(SearchParamsDto searchParamsDto, int pageNumber, int pageSize) {
        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by("name").ascending());
        ProductSearchSpecification productSearchSpecification = new ProductSearchSpecification(searchParamsDto);
        return productRepository.findAll(productSearchSpecification, paging).stream().map(productConverter::toDto).toList();
    }

    @Override
    public List<ProductDto> getProductByCategoryId(int id, int pageNumber, int pageSize) {
        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by("id").ascending());
        categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Category with id %d not found", id)));
        Page<Product> products = productRepository.findAllByCategoryId(id, paging);
        return products.getContent().stream().map(productConverter::toDto).toList();
    }

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        Product product = productConverter.fromDto(productDto);
        product = productRepository.save(product);
        return productConverter.toDto(product);
    }

    @Override
    public void deleteProduct(int id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Product with id %d not found", id)));
        productRepository.delete(product);
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto) {
        Product product = productRepository.findById(productDto.getId())
                .orElseThrow(() -> new EntityNotFoundException(String.format("Product with id %d not found", productDto.getId())));
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        return productConverter.toDto(productRepository.save(product));
    }

    @Override
    public ProductDto getProductById(int id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Product with id %d not found", id)));
        return productConverter.toDto(product);
    }

    @Override
    public void downloadProductsToFile(List<ProductDto> products, HttpServletResponse response)
            throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        int categoryId = products.get(0).getCategoryId();
        try (Writer writer = new OutputStreamWriter(response.getOutputStream())) {
            StatefulBeanToCsv<ProductDto> beanToCsv = new StatefulBeanToCsvBuilder<ProductDto>(writer)
                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                    .withSeparator(',')
                    .build();
            response.setContentType("text/csv");
            response.setHeader("Content-Disposition", "attachment; filename="
                    + String.format("Category %d - products.csv", categoryId));
            beanToCsv.write(products);
        }
    }

    @Override
    public List<ProductDto> uploadProductsFromFile(MultipartFile file) throws IOException {
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            CsvToBean<ProductDto> csvToBean = new CsvToBeanBuilder<ProductDto>(reader)
                    .withType(ProductDto.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withIgnoreQuotations(true)
                    .withSeparator(',')
                    .build();
            List<ProductDto> productDtoList = new ArrayList<>();
            List<Product> products = new ArrayList<>();
            csvToBean.forEach(productDtoList::add);
            for (ProductDto dto : productDtoList) {
                Product p = productRepository.save(productConverter.fromDto(dto));
                products.add(p);
            }
            return products.stream().map(productConverter::toDto).toList();
        }
    }
}
