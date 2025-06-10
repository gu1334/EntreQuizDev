package br.com.entrequizdev.user.dto;

import br.com.entrequizdev.user.enums.AreasEnum;

public record CreateUserDto(

        String name,
        String email,
        String password,
        AreasEnum areas

) {
}
