package com.example.restwebservice.repositories;

import com.example.restwebservice.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
