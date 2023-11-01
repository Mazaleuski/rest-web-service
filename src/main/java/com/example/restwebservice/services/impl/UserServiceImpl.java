package com.example.restwebservice.services.impl;

import com.example.restwebservice.dto.UserDto;
import com.example.restwebservice.dto.converters.UserConverter;
import com.example.restwebservice.entities.User;
import com.example.restwebservice.exceptions.AuthorizationException;
import com.example.restwebservice.repositories.UserRepository;
import com.example.restwebservice.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(userConverter::toDto).toList();
    }

    @Override
    public UserDto getUserById(int id) {
        User user = Optional.ofNullable(userRepository.findById(id))
                .orElseThrow(() -> new EntityNotFoundException(String.format("User with id %d not found", id)));
        return userConverter.toDto(user);
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = userConverter.fromDto(userDto);
        user = userRepository.createOrUpdateUser(user);
        return userConverter.toDto(user);
    }

    @Override
    public UserDto loginUser(UserDto userDto) throws AuthorizationException {
        User user = Optional.ofNullable(userRepository.findByEmailAndPassword(userDto.getEmail(), userDto.getPassword()))
                .orElseThrow(() -> new AuthorizationException(String.format("User with email %s not registered", userDto.getEmail())));
        return userConverter.toDto(userRepository.createOrUpdateUser(user));
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        User user = Optional.ofNullable(userRepository.findById(userDto.getId()))
                .orElseThrow(() -> new EntityNotFoundException(String.format("User with id %d not found", userDto.getId())));
        user.setAddress(userDto.getAddress());
        user.setPhoneNumber(userDto.getPhoneNumber());
        return userConverter.toDto(userRepository.createOrUpdateUser(user));
    }

    @Override
    public void deleteUser(int id) {
        userRepository.delete(id);
    }
}
