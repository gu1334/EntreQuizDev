package br.com.entrequizdev.user.dto;

public record CreateUserDto(

        String name,
        String email,
        String password

) {
}
