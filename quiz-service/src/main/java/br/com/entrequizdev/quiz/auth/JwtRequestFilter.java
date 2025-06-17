package br.com.entrequizdev.quiz.auth;

import br.com.entrequizdev.quiz.dto.TokenValidationResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity; // Importar ResponseEntity
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors; // Importar Collectors

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private AuthClient authClient;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            try {
                // 1. Chamar authClient.validateToken e obter TokenValidationResponse
                ResponseEntity<TokenValidationResponse> authResponse = authClient.validateToken(token);

                if (authResponse.getStatusCode() == HttpStatus.OK && authResponse.getBody() != null) {
                    TokenValidationResponse validationResponse = authResponse.getBody();

                    // 2. Extrair roles e username
                    String username = validationResponse.getUsername();
                    List<String> roles = validationResponse.getRoles();

                    // 3. Converter para SimpleGrantedAuthority
                    List<SimpleGrantedAuthority> authorities = roles.stream()
                            .map(role -> new SimpleGrantedAuthority("ROLE_" + role)) // Importante: Spring Security espera "ROLE_" prefixo
                            .collect(Collectors.toList());

                    // 4. Usar para criar UsernamePasswordAuthenticationToken
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    username, // Usar o username retornado pelo serviço de usuário
                                    null, // Credenciais nulas, pois já foram validadas pelo JWT
                                    authorities // Usar as authorities obtidas do token
                            );

                    SecurityContextHolder.getContext().setAuthentication(authentication);

                } else {
                    // Se a resposta do serviço de autenticação não for OK
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    response.getWriter().write("Falha na validação do token pelo serviço de autenticação.");
                    return;
                }

            } catch (Exception e) {
                // Logar a exceção para depuração
                e.printStackTrace();
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write("Token inválido ou expirado.");
                return;
            }
        } else {
            // Se o token estiver ausente ou mal formatado
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Token ausente ou formato inválido (Bearer Token esperado).");
            return;
        }

        filterChain.doFilter(request, response);
    }
}