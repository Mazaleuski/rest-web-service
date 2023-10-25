package com.example.restwebservice.repositories;

import com.example.restwebservice.entities.Product;

import java.util.List;

public interface ProductRepository {

    Product findById(int id);

    List<Product> findByCategoryId(int id);

    Product createOrUpdateProduct(Product product);

    List<Product> findByNameOrDescription(String search);

    List<Product> findAll();

    void delete(int id);
}
