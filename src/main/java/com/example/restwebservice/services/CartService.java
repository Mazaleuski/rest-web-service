package com.example.restwebservice.services;

import com.example.restwebservice.dto.CartDto;
import com.example.restwebservice.dto.converters.ProductConverter;
import com.example.restwebservice.entities.Product;
import com.example.restwebservice.repositories.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class CartService {

    private final ProductRepository productRepository;
    private final ProductConverter productConverter;

    public CartDto addProduct(int id, CartDto cartDto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Product with id: %d not found", id)));
        cartDto.addProduct(productConverter.toDto(product));
        return cartDto;
    }

    public CartDto removeProduct(int id, CartDto cartDto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Product with id: %d not found", id)));
        cartDto.removeProduct(productConverter.toDto(product));
        return cartDto;
    }

    public CartDto clear(CartDto cartDto) {
        cartDto.clear();
        return cartDto;
    }
}
