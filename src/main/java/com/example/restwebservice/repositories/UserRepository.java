package com.example.restwebservice.repositories;

import com.example.restwebservice.entities.User;

import java.util.List;

public interface UserRepository {

    User createOrUpdateUser(User user);

    List<User> findAll();

    User findById(int id);

    User findByEmailAndPassword(String email, String password);

    void delete(int id);
}
