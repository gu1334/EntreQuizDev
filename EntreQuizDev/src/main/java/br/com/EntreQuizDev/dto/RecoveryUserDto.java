package br.com.EntreQuizDev.dto;

import br.com.EntreQuizDev.entity.Role;

import java.util.List;

public record RecoveryUserDto(

        Long id,
        String email,
        String senha

) {
}