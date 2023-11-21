package com.example.restwebservice.repositories;

import com.example.restwebservice.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmailAndPassword(String email, String password);
}
