package com.example.restwebservice.services;

import com.example.restwebservice.dto.UserDto;
import com.example.restwebservice.exceptions.AuthorizationException;

import java.util.List;

public interface UserService {

    List<UserDto> getAllUsers();

    UserDto getUserById(int id);

    UserDto createUser(UserDto userDto);

    UserDto loginUser(UserDto userDto) throws AuthorizationException;

    UserDto updateUser(UserDto userDto);

    void deleteUser(int id);
}
