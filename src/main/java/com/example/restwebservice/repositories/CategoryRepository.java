package com.example.restwebservice.repositories;

import com.example.restwebservice.entities.Category;

import java.util.List;

public interface CategoryRepository {

    Category findById(int id);

    List<Category> findAll();

    Category createOrUpdateCategory(Category category);

    void delete(int id);
}
