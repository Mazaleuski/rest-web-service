package com.example.restwebservice.services.impl;

import com.example.restwebservice.config.JwtProvider;
import com.example.restwebservice.dto.JwtRequest;
import com.example.restwebservice.dto.JwtResponse;
import com.example.restwebservice.dto.UserDto;
import com.example.restwebservice.dto.converters.UserConverter;
import com.example.restwebservice.entities.User;
import com.example.restwebservice.exceptions.AuthorizationException;
import com.example.restwebservice.repositories.UserRepository;
import com.example.restwebservice.services.UserService;
import io.jsonwebtoken.Claims;
import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private static final Map<String, String> refreshStorage = new HashMap<>();

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(userConverter::toDto).toList();
    }

    @Override
    public UserDto getUserById(int id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("User with id %d not found", id)));
        return userConverter.toDto(user);
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = userConverter.fromDto(userDto);
        user = userRepository.save(user);
        return userConverter.toDto(user);
    }


    @Override
    public JwtResponse login(@NonNull JwtRequest request) throws AuthorizationException {
        UserDto user = userRepository.findByEmail(request.getEmail()).map(userConverter::toDto)
                .orElseThrow(() -> new AuthorizationException("User not found"));
        if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            String accessToken = jwtProvider.generateAccessToken(user);
            String refreshToken = jwtProvider.generateRefreshToken(user);
            refreshStorage.put(user.getEmail(), refreshToken);
            return new JwtResponse(accessToken, refreshToken);
        } else {
            throw new AuthorizationException("Invalid password");
        }
    }

    @Override
    public JwtResponse getAccessToken(@NonNull String refreshToken) throws AuthorizationException {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String email = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(email);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                UserDto user = userRepository.findByEmail(email).map(userConverter::toDto)
                        .orElseThrow(() -> new AuthorizationException("User not found"));
                final String accessToken = jwtProvider.generateAccessToken(user);
                return new JwtResponse(accessToken, refreshToken);
            }
        }
        return new JwtResponse(null, null);
    }


    @Override
    public JwtResponse getRefreshToken(@NonNull String refreshToken) throws AuthorizationException {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String email = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(email);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                UserDto user = userRepository.findByEmail(email).map(userConverter::toDto)
                        .orElseThrow(() -> new AuthorizationException("User not found"));
                final String accessToken = jwtProvider.generateAccessToken(user);
                final String newRefreshToken = jwtProvider.generateRefreshToken(user);
                refreshStorage.put(user.getEmail(), newRefreshToken);
                return new JwtResponse(accessToken, newRefreshToken);
            }
        }
        throw new AuthorizationException("Invalid jwt token");
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        User user = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new EntityNotFoundException(String.format("User with id %d not found", userDto.getId())));
        user.setAddress(userDto.getAddress());
        user.setPhoneNumber(userDto.getPhoneNumber());
        return userConverter.toDto(userRepository.save(user));
    }

    @Override
    public void deleteUser(int id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("User with id %d not found", id)));
        userRepository.delete(user);
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return userRepository.findByEmail(login);
    }
}

