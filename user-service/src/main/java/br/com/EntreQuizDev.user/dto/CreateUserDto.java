package br.com.EntreQuizDev.user.dto;

import br.com.EntreQuizDev.user.enums.RoleName;

public record CreateUserDto(

        String name,
        String email,
        String password

) {
}
