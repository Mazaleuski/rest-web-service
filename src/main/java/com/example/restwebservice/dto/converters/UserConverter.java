package com.example.restwebservice.dto.converters;

import com.example.restwebservice.dto.UserDto;
import com.example.restwebservice.entities.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class UserConverter {

    private final OrderConverter orderConverter;

    public UserDto toDto(User user) {
        return Optional.ofNullable(user).map(u -> UserDto.builder()
                        .id(u.getId())
                        .name(u.getName())
                        .surname(u.getSurname())
                        .birthday(u.getBirthday())
                        .email(u.getEmail())
                        .password(u.getPassword())
                        .balance(u.getBalance())
                        .address(u.getAddress())
                        .phoneNumber(u.getPhoneNumber())
                        .orders(Optional.ofNullable(u.getOrder()).map(o -> o
                                .stream().map(orderConverter::toDto).toList()).orElse(List.of()))
                        .build())
                .orElse(null);
    }

    public User fromDto(UserDto userDto) {
        return Optional.ofNullable(userDto).map(u -> User.builder()
                        .name(u.getName())
                        .surname(u.getSurname())
                        .birthday(u.getBirthday())
                        .email(u.getEmail())
                        .password(u.getPassword())
                        .balance(u.getBalance())
                        .address(u.getAddress())
                        .phoneNumber(u.getPhoneNumber())
                        .order(Optional.ofNullable(u.getOrders()).map(o -> o
                                .stream().map(orderConverter::fromDto).toList()).orElse(List.of()))
                        .build())
                .orElse(null);
    }
}
