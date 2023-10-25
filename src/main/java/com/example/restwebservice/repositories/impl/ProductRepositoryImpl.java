package com.example.restwebservice.repositories.impl;

import com.example.restwebservice.entities.Product;
import com.example.restwebservice.repositories.ProductRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
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
public class ProductRepositoryImpl implements ProductRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    private static final String GET_ALL_PRODUCTS = "select p from Product p";
    private static final String GET_PRODUCT_BY_NAME_OR_DESCRIPTION =
            "from Product where name like :search or description like :search";
    private static final String GET_PRODUCTS_BY_CATEGORY_ID = "select p from Product p where p.category.id=:categoryId";

    @Override
    public Product findById(int id) {
        return entityManager.find(Product.class, id);
    }

    @Override
    public List<Product> findByCategoryId(int id) {
        TypedQuery<Product> query = entityManager.createQuery(GET_PRODUCTS_BY_CATEGORY_ID, Product.class);
        query.setParameter("categoryId", id);
        return query.getResultList();
    }

    @Override
    public Product createOrUpdateProduct(Product product) {
        return entityManager.merge(product);
    }

    @Override
    public List<Product> findByNameOrDescription(String search) {
        TypedQuery<Product> query = entityManager.createQuery(GET_PRODUCT_BY_NAME_OR_DESCRIPTION, Product.class);
        query.setParameter("search", "%" + search + "%");
        return query.getResultList();
    }

    @Override
    public List<Product> findAll() {
        return entityManager.createQuery(GET_ALL_PRODUCTS, Product.class).getResultList();
    }

    @Override
    public void delete(int id) {
        Product product = Optional.ofNullable(entityManager.find(Product.class, id))
                .orElseThrow(() -> new EntityNotFoundException(String.format("Product with id %d not found", id)));
        entityManager.remove(product);
    }
}
