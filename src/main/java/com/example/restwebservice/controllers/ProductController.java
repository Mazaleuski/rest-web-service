package com.example.restwebservice.controllers;

import com.example.restwebservice.dto.ProductDto;
import com.example.restwebservice.services.ProductService;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
@Validated
@Tag(name = "product", description = "Product Endpoints")

public class ProductController {
    private final ProductService productService;

    @Operation(
            summary = "Find all products",
            description = "Find all existed products in shop",
            tags = {"product"})
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "All products were found",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductDto.class)))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Products not found - server error"
            )
    })

    @GetMapping("/all")
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
    }

    @Operation(
            summary = "Create product",
            description = "Create new product",
            tags = {"product"})
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Product was created",
                    content = @Content(schema = @Schema(contentSchema = ProductDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Product not created - server error"
            )
    })

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody @Valid ProductDto productDto) {
        return new ResponseEntity<>(productService.createProduct(productDto), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Update product",
            description = "Update existed product",
            tags = {"product"})
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Product was updated",
                    content = @Content(schema = @Schema(contentSchema = ProductDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Product not updated - server error",
                    content = @Content(schema = @Schema(implementation = String.class))
            )
    })

    @PutMapping
    public ResponseEntity<ProductDto> updateProduct(@RequestBody @Valid ProductDto productDto) {
        return new ResponseEntity<>(productService.updateProduct(productDto), HttpStatus.OK);
    }

    @Operation(
            summary = "Delete product",
            description = "Delete existed product",
            tags = {"product"})
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Product was deleted"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Product not deleted - server error"
            )
    })

    @DeleteMapping("/{id}")
    public void deleteProduct(@Parameter(required = true, description = "Product id") @PathVariable @Positive int id) {
        productService.deleteProduct(id);
    }

    @Operation(
            summary = "Find certain product",
            description = "Find certain existed product in shop by id",
            tags = {"product"})
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Product was found by id",
                    content = @Content(schema = @Schema(contentSchema = ProductDto.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Category not found - forbidden operation",
                    content = @Content(schema = @Schema(implementation = String.class))
            )
    })

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@Parameter(required = true, description = "Product id") @PathVariable @Positive int id) {
        return Optional.ofNullable(productService.getProductById(id))
                .map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(
            summary = "Find products by category",
            description = "Find certain existed products in shop by category id",
            tags = {"product"})
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Products were found by category id",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductDto.class)))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Products not fount - forbidden operation",
                    content = @Content(schema = @Schema(implementation = String.class))
            )
    })

    @GetMapping("/category/{id}")
    public ResponseEntity<List<ProductDto>> getProductByCategoryId(@Parameter(required = true, description = "Category id")
                                                                   @PathVariable @Positive int id) {
        return Optional.ofNullable(productService.getProductByCategoryId(id))
                .map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(
            summary = "Search products",
            description = "Search existed products in shop by string",
            tags = {"product"})
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Products were found by string",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductDto.class)))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Products not fount - forbidden operation"
            )
    })

    @GetMapping("/search/{search}")
    public ResponseEntity<List<ProductDto>> searchProduct(@Parameter(required = true, description = "String for search")
                                                          @PathVariable String search) {
        return Optional.ofNullable(productService.searchProducts(search))
                .map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(
            summary = "Save products to file",
            description = "Save products to .csv file")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Products were saved"
            )
    })

    @PostMapping("/download")
    public void downloadProductsToFile(@RequestBody List<ProductDto> products,
                                       @Parameter(description = "Path for download") @RequestParam String path)
            throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {
        productService.downloadProductsToFile(products, path);
    }

    @Operation(
            summary = "Upload products from file",
            description = "Upload products from .csv file")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "products were upload",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductDto.class)))
            )
    })

    @PostMapping("/upload")
    public ResponseEntity<List<ProductDto>> uploadProductsFromFile(@Parameter(description = "File for upload ")
                                                                   @RequestParam("file") MultipartFile file) throws Exception {
        return new ResponseEntity<>(productService.uploadProductsFromFile(file), HttpStatus.CREATED);
    }
}
