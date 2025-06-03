package br.com.entrequizdev.user.dto;


import java.util.List;

public record RecoveryUserDto(

        Long id,
        String email,
        String senha

) {
}