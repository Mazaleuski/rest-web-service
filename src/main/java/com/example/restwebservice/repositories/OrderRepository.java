package com.example.restwebservice.repositories;

import com.example.restwebservice.entities.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    Page<Order> findAllByUserId(int id, Pageable paging);
}
