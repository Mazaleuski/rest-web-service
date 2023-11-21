package com.example.restwebservice.repositories;

import com.example.restwebservice.dto.SearchParamsDto;
import com.example.restwebservice.entities.Category;
import com.example.restwebservice.entities.Product;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class ProductSearchSpecification implements Specification<Product> {

    private final SearchParamsDto searchParamsDto;

    @Override
    public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        if (Optional.ofNullable(searchParamsDto.getSearchKey()).isPresent() && !searchParamsDto.getSearchKey().isBlank()) {
            predicates.add(criteriaBuilder
                    .or(criteriaBuilder.like(root.get("name"), "%" + searchParamsDto.getSearchKey() + "%"),
                            criteriaBuilder.like(root.get("description"), "%" + searchParamsDto.getSearchKey() + "%")));
        }

        if (searchParamsDto.getPriceFrom() > 0) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), searchParamsDto.getPriceFrom()));
        }

        if (searchParamsDto.getPriceTo() > 0) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), searchParamsDto.getPriceTo()));
        }

        if (Optional.ofNullable(searchParamsDto.getCategoryName()).isPresent()
                && !searchParamsDto.getCategoryName().isBlank()) {
            Join<Product, Category> productCategoryJoin = root.join("category");
            predicates.add(criteriaBuilder.and(criteriaBuilder.like(productCategoryJoin.get("name"),
                    "%" + searchParamsDto.getCategoryName() + "%")));
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}

