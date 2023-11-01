package com.example.restwebservice.repositories;

import com.example.restwebservice.entities.Order;

import java.util.List;

public interface OrderRepository {

    Order createOrUpdateOrder(Order order);

    Order findById(int id);

    void delete(int id);

    List<Order> findByUserId(int id);
}
