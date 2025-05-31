package br.com.EntreQuizDev.config;

import br.com.EntreQuizDev.entity.Usuario;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

//O UserDetails é uma interface que representa os detalhes
// do usuário, como seu nome de usuário, senha e autorizações.

//Já o UserDetailsService é uma interface que retorna um UserDetails com
// base no nome de usuário fornecido. Juntas, essas interfaces fornecem uma maneira
// de obter e validar os detalhes do usuário durante o processo de autenticação

@Getter
public class UserDetailsImpl implements UserDetails {

    private Usuario usuario;

    public UserDetailsImpl(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        /*
         Este método converte a lista de papéis (roles) associados ao usuário 
         em uma coleção de GrantedAuthorities, que é a forma que o Spring Security 
         usa para representar papéis. Isso é feito mapeando cada papel para um 
         novo SimpleGrantedAuthority, que é uma implementação simples de 
         GrantedAuthority
        */
        return usuario.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return usuario.getSenha();
    } // Retorna a credencial do usuário que criamos anteriormente

    @Override
    public String getUsername() {
        return usuario.getEmail();
    } // Retorna o nome de usuário do usuário que criamos anteriormente

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}