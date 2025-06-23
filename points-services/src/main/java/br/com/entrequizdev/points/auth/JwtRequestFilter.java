package br.com.entrequizdev.points.auth;

import br.com.entrequizdev.points.dto.TokenValidationResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.util.AntPathMatcher; // Importar AntPathMatcher

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Arrays; // Importar Arrays

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private AuthClient authClient;

    // Defina os caminhos que este filtro deve ignorar.
    // DEVE SER OS MESMOS CAMINHOS QUE ESTÃO EM ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED no SecurityConfig
    private static final String[] AUTH_WHITELIST = {
            "/auth/pergunta",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/v3/api-docs.yaml"
    };

    private AntPathMatcher pathMatcher = new AntPathMatcher(); // Usar AntPathMatcher para corresponder padrões de URL

    // Sobrescreve o método shouldNotFilter para ignorar certos caminhos
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String requestUri = request.getRequestURI();
        return Arrays.stream(AUTH_WHITELIST).anyMatch(pattern -> pathMatcher.match(pattern, requestUri));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // ... (resto do seu código do doFilterInternal permanece o mesmo) ...

        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            try {
                ResponseEntity<TokenValidationResponse> authResponse = authClient.validateToken(token);

                if (authResponse.getStatusCode() == HttpStatus.OK && authResponse.getBody() != null) {
                    TokenValidationResponse validationResponse = authResponse.getBody();
                    String username = validationResponse.getUsername();
                    List<String> roles = validationResponse.getRoles();

                    List<SimpleGrantedAuthority> authorities = roles.stream()
                            .map(SimpleGrantedAuthority::new) // Apenas crie a autoridade com a string literal.
                            .collect(Collectors.toList());

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    username,
                                    null,
                                    authorities
                            );

                    SecurityContextHolder.getContext().setAuthentication(authentication);

                } else {
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    response.getWriter().write("Falha na validação do token pelo serviço de autenticação.");
                    return;
                }

            } catch (Exception e) {
                e.printStackTrace();
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write("Token inválido ou expirado.");
                return;
            }
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Token ausente ou formato inválido (Bearer Token esperado).");
            return;
        }

        filterChain.doFilter(request, response);
    }
}