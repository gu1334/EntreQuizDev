package br.com.entrequizdev.user.dto;

import java.util.List;

public record UserResponseDto(
        String name,
        String email,
        String areas,
        List<String> roles
) {}
