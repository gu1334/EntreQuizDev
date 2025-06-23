package br.com.entrequizdev.user.config;

import br.com.entrequizdev.user.entity.Usuario;
import br.com.entrequizdev.user.repository.UsuarioRepository;
import br.com.entrequizdev.user.service.JwtTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails; // Importar UserDetails
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
public class UserAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // >>>>>> POR FAVOR, ADICIONE ESTA LINHA AQUI <<<<<<
        System.out.println("DEBUG - UserAuthenticationFilter foi acionado para URI: " + request.getRequestURI());

        // Verifica se o endpoint requer autenticação antes de processar a requisição
        if (checkIfEndpointIsNotPublic(request)) {
            String token = recoveryToken(request);
            if (token != null) {
                String subject = jwtTokenService.getSubjectFromToken(token);

                if (subject != null) {
                    Usuario usuario = usuarioRepository.findByEmail(subject).orElse(null);
                    if (usuario != null) {
                        UserDetails userDetails = new UserDetailsImpl(usuario);

                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        System.out.println("DEBUG - Authorities in SecurityContext for user " + authentication.getName() + ": " + authentication.getAuthorities());

                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    } else {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getWriter().write("Usuário do token não encontrado.");
                        return;
                    }
                } else {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Token inválido ou com assunto ausente.");
                    return;
                }
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("O token de autenticação está ausente.");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    // Recupera o token do cabeçalho Authorization da requisição
    private String recoveryToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.replace("Bearer ", "");
        }
        return null;
    }

    // Verifica se o endpoint requer autenticação antes de processar a requisição
    // Agora verifica se a URI da requisição começa com algum dos padrões públicos
    private boolean checkIfEndpointIsNotPublic(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return Arrays.stream(SecurityConfiguration.ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED)
                .noneMatch(publicEndpoint -> requestURI.startsWith(publicEndpoint.replace("/**", "")));
        // Explicação:
        // .noneMatch(...) significa "nenhum dos endpoints públicos corresponde a esta requisição"
        // publicEndpoint.replace("/**", ""): Remove o "/**" para comparar apenas a parte inicial do caminho.
        // Ex: "/swagger-ui/**" vira "/swagger-ui".
        // requestURI.startsWith(...): Verifica se a URI da requisição começa com a parte inicial do padrão público.
    }
}