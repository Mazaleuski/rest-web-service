package com.example.restwebservice.repositories.impl;

import com.example.restwebservice.entities.Category;
import com.example.restwebservice.repositories.CategoryRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
@AllArgsConstructor
@Transactional
public class CategoryRepositoryImpl implements CategoryRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    private static final String GET_ALL_CATEGORIES = "select c from Category c";

    @Override
    public Category findById(int id) {
        return entityManager.find(Category.class, id);
    }

    @Override
    public List<Category> findAll() {
        return entityManager.createQuery(GET_ALL_CATEGORIES, Category.class).getResultList();
    }

    @Override
    public Category createOrUpdateCategory(Category category) {
        return entityManager.merge(category);
    }

    @Override
    public void delete(int id) {
        Category category = Optional.ofNullable(entityManager.find(Category.class, id))
                .orElseThrow(() -> new EntityNotFoundException(String.format("Category with id %d not found", id)));
        entityManager.remove(category);
    }
}
