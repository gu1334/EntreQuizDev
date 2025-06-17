package br.com.entrequizdev.user.dto;

import java.util.List;

// Crie esta classe em um pacote adequado, ex: br.com.entrequizdev.user.dto
public class TokenValidationResponse {
    private String username;
    private List<String> roles;

    // Construtores, getters e setters
    public TokenValidationResponse(String username, List<String> roles) {
        this.username = username;
        this.roles = roles;
    }

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