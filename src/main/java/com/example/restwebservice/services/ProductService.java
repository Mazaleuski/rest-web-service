package com.example.restwebservice.services;

import com.example.restwebservice.dto.ProductDto;
import com.example.restwebservice.dto.SearchParamsDto;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService {

    List<ProductDto> getAllProducts(int pageNumber,int pageSize);

    List<ProductDto> searchProducts(SearchParamsDto searchParamsDto,int pageNumber,int pageSize);

    List<ProductDto> getProductByCategoryId(int id,int pageNumber,int pageSize);

    ProductDto createProduct(ProductDto productDto);

    void deleteProduct(int id);

    ProductDto updateProduct(ProductDto productDto);

    ProductDto getProductById(int id);

    void downloadProductsToFile(List<ProductDto> products, HttpServletResponse response) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException;

    List<ProductDto> uploadProductsFromFile(MultipartFile file) throws IOException;
}
