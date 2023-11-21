package com.example.restwebservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class JwtResponse {
    private final String type = "Bearer";

    private String accessToken;

    private String refreshToken;
}
