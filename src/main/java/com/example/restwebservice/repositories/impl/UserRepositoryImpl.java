package com.example.restwebservice.repositories.impl;

import com.example.restwebservice.entities.User;
import com.example.restwebservice.repositories.UserRepository;
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
public class UserRepositoryImpl implements UserRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    private static final String GET_ALL_USERS = "select u from User u";
    private static final String GET_USER_BY_EMAIL_AND_PASSWORD = "select u from User u where u.email=:email and u.password=:password";

    @Override
    public User createOrUpdateUser(User user) {
        return entityManager.merge(user);
    }

    @Override
    public List<User> findAll() {
        return entityManager.createQuery(GET_ALL_USERS, User.class).getResultList();
    }

    @Override
    public User findById(int id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public User findByEmailAndPassword(String email, String password) {
        TypedQuery<User> query = entityManager.createQuery(GET_USER_BY_EMAIL_AND_PASSWORD, User.class);
        query.setParameter("email", email);
        query.setParameter("password", password);
        return query.getSingleResult();
    }

    @Override
    public void delete(int id) {
        User user = Optional.ofNullable(entityManager.find(User.class, id))
                .orElseThrow(() -> new EntityNotFoundException(String.format("User with id %d not found", id)));
        entityManager.remove(user);
    }
}

