package com.example.restwebservice.repositories.impl;

import com.example.restwebservice.entities.Order;
import com.example.restwebservice.repositories.OrderRepository;
import com.example.restwebservice.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
@AllArgsConstructor

public class OrderRepositoryImpl implements OrderRepository {

    @PersistenceContext
    private final EntityManager entityManager;
    private final UserRepository userRepository;

    private static final String GET_ORDERS_BY_USER = "select o from Order o where o.user=:user";

    @Override
    public Order createOrUpdateOrder(Order order) {
        return entityManager.merge(order);
    }

    @Override
    public Order findById(int id) {
        return entityManager.find(Order.class, id);
    }

    @Override
    public void delete(int id) {
        Order order = Optional.ofNullable(entityManager.find(Order.class, id))
                .orElseThrow(() -> new EntityNotFoundException(String.format("Order with id %d not found", id)));
        entityManager.remove(order);
    }

    @Override
    public List<Order> findByUserId(int id) {
        TypedQuery<Order> query = entityManager.createQuery(GET_ORDERS_BY_USER, Order.class);
        query.setParameter("user", userRepository.findById(id));
        return query.getResultList();
    }
}
