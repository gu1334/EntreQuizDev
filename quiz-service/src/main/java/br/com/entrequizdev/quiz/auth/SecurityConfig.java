package br.com.entrequizdev.quiz.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    // Endpoints que não requerem autenticação (acesso público)
    public static final String[] ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED = {
            "/teste/publico",
            "/auth/pergunta", // Manter o endpoint de validação de token como público
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/v3/api-docs.yaml"
    };

    // Endpoints que requerem autenticação, mas não uma role específica
    public static final String[] ENDPOINTS_WITH_AUTHENTICATION_REQUIRED = {
            "/resposta",
            "/resposta/**"
    };

    // Endpoints que só podem ser acessados por usuários com a permissão de JOGADOR
    public static final String[] ENDPOINTS_JOGADOR = {
    };

    // Endpoints que só podem ser acessados por usuários com a permissão de ADMINISTRATOR
    public static final String[] ENDPOINTS_ADMIN = {
            "/perguntas/**",
            "/perguntas"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable() // Desativa a proteção contra CSRF
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Configura a política de criação de sessão como stateless
                .and()
                .authorizeHttpRequests() // Habilita a autorização para as requisições HTTP
                .requestMatchers(ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED).permitAll() // Permite acesso público
                .requestMatchers(ENDPOINTS_WITH_AUTHENTICATION_REQUIRED).authenticated() // Requer autenticação
                .requestMatchers(ENDPOINTS_ADMIN).hasAuthority("ROLE_ADMINISTRATOR") // Mude hasRole para hasAuthority
                .requestMatchers(ENDPOINTS_JOGADOR).hasAuthority("ROLE_JOGADOR")    // E também para JOGADOR, se tiver
                .anyRequest().denyAll() // Nega qualquer outra requisição que não foi explicitamente permitida acima
                .and()
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class) // Adiciona nosso filtro JWT
                .build();
    }
}