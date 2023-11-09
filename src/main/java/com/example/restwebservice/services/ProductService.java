package com.example.restwebservice.services;

import com.example.restwebservice.dto.ProductDto;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService {

    List<ProductDto> getAllProducts();

    List<ProductDto> searchProducts(String search);

    List<ProductDto> getProductByCategoryId(int id);

    ProductDto createProduct(ProductDto productDto);

    void deleteProduct(int id);

    ProductDto updateProduct(ProductDto productDto);

    ProductDto getProductById(int id);

    void downloadProductsToFile(List<ProductDto> products, HttpServletResponse response) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException;

    List<ProductDto> uploadProductsFromFile(MultipartFile file) throws IOException;
}
