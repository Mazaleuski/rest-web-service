package com.example.restwebservice.services;

import com.example.restwebservice.dto.JwtRequest;
import com.example.restwebservice.dto.JwtResponse;
import com.example.restwebservice.dto.UserDto;
import com.example.restwebservice.entities.User;
import com.example.restwebservice.exceptions.AuthorizationException;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<UserDto> getAllUsers();

    UserDto getUserById(int id);

    UserDto createUser(UserDto userDto);

    JwtResponse login(@NonNull JwtRequest request) throws AuthorizationException;

    JwtResponse getAccessToken(@NonNull String refreshToken) throws AuthorizationException;

    JwtResponse getRefreshToken(@NonNull String refreshToken) throws AuthorizationException;

    UserDto updateUser(UserDto userDto);

    void deleteUser(int id);

    Optional<User> findByLogin(String login);
}
