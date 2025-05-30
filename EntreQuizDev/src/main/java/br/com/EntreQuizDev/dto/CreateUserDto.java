package br.com.EntreQuizDev.dto;

import br.com.EntreQuizDev.enums.RoleName;

public record CreateUserDto(

        String name,
        String email,
        String password

) {
}
