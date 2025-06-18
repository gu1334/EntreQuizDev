package br.com.entrequizdev.quiz.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

// Crie esta classe em um pacote adequado, ex: br.com.entrequizdev.user.dto
@AllArgsConstructor
@NoArgsConstructor
public class TokenValidationResponse {
    private String username;
    private List<String> roles;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}