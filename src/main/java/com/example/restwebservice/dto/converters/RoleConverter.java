package com.example.restwebservice.dto.converters;

import com.example.restwebservice.dto.RoleDto;
import com.example.restwebservice.entities.Role;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RoleConverter {

    public RoleDto toDto(Role role) {
        return Optional.ofNullable(role).map(r -> RoleDto.builder()
                        .id(r.getId())
                        .name(r.getName())
                        .build())
                .orElse(null);
    }

    public Role fromDto(RoleDto roleDto) {
        return Optional.of(roleDto).map(r -> Role.builder()
                        .name(r.getName())
                        .build())
                .orElse(null);
    }
}
