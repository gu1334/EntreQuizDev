package br.com.entrequizdev.user.dto;

import br.com.entrequizdev.user.enums.RoleName;

public record ChangeUser(
        String name,
        String email,
        String password,
        RoleName roleName
) {
}
