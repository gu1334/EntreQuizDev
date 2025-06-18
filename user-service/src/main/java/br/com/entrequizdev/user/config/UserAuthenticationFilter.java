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
    private JwtTokenService jwtTokenService; // Service que definimos anteriormente

    @Autowired
    private UsuarioRepository usuarioRepository; // Repository que definimos anteriormente

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Verifica se o endpoint requer autenticação antes de processar a requisição
        if (checkIfEndpointIsNotPublic(request)) {
            String token = recoveryToken(request); // Recupera o token do cabeçalho Authorization da requisição
            if (token != null) {
                String subject = jwtTokenService.getSubjectFromToken(token); // Obtém o assunto (neste caso, o nome de usuário) do token

                // Garante que o usuário exista antes de tentar carregar UserDetails
                if (subject != null) {
                    Usuario usuario = usuarioRepository.findByEmail(subject).orElse(null); // Usar orElse(null) para evitar NoSuchElementException
                    if (usuario != null) {
                        // UserDetailsImpl é o nome da sua implementação de UserDetails
                        UserDetails userDetails = new UserDetailsImpl(usuario);

                        // Cria um objeto de autenticação do Spring Security
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        System.out.println("DEBUG - Authorities in SecurityContext for user " + authentication.getName() + ": " + authentication.getAuthorities());

                        // Define o objeto de autenticação no contexto de segurança do Spring Security
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    } else {
                        // Se o usuário não for encontrado pelo email do token
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
                        response.getWriter().write("Usuário do token não encontrado.");
                        return; // Impede que a requisição continue
                    }
                } else {
                    // Se o assunto (email) não puder ser extraído do token
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
                    response.getWriter().write("Token inválido ou com assunto ausente.");
                    return; // Impede que a requisição continue
                }
            } else {
                // Se o token estiver ausente E o endpoint não for público
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
                response.getWriter().write("O token de autenticação está ausente.");
                return; // Impede que a requisição continue
                // throw new RuntimeException("O token está ausente."); // Evite RuntimeException em filtros, prefira configurar o response
            }
        }
        filterChain.doFilter(request, response); // Continua o processamento da requisição
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