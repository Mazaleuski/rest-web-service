package com.example.restwebservice.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CartDto {

    private final List<ProductDto> products;
    private int totalPrice;

    public CartDto() {
        this.products = new ArrayList<>();
    }

    public void addProduct(ProductDto productDto) {
        products.add(productDto);
        totalPrice += productDto.getPrice();
    }

    public void removeProduct(ProductDto productDto) {
        products.remove(productDto);
        if (productDto != null) {
            totalPrice -= productDto.getPrice();
        }
    }

    public void clear() {
        products.clear();
        totalPrice = 0;
    }
}
