package br.com.entrequizdev.user.service;

import br.com.entrequizdev.user.config.SecurityConfiguration;
import br.com.entrequizdev.user.config.UserDetailsImpl;
import br.com.entrequizdev.user.dto.*;
import br.com.entrequizdev.user.entity.Role;
import br.com.entrequizdev.user.entity.Usuario;
import br.com.entrequizdev.user.enums.AreasEnum;
import br.com.entrequizdev.user.enums.RoleName;
import br.com.entrequizdev.user.exception.DadosInvalidosException;
import br.com.entrequizdev.user.repository.RoleRepository;
import br.com.entrequizdev.user.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private SecurityConfiguration securityConfiguration;

    @Autowired
    private RoleRepository roleRepository; // agora você pode buscar a role pelo enum


    // Método responsável por autenticar um usuário e retornar um token JWT
    public RecoveryJwtTokenDto authenticateUser(LoginUserDto loginUserDto) {
        // Cria um objeto de autenticação com o email e a senha do usuário
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(loginUserDto.email(), loginUserDto.password());

        // Autentica o usuário com as credenciais fornecidas
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        // Obtém o objeto UserDetails do usuário autenticado
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // Gera um token JWT para o usuário autenticado
        return new RecoveryJwtTokenDto(jwtTokenService.generateToken(userDetails));
    }

    // Método responsável por criar um usuário
    public void createUser(CreateUserDto createUserDto) {
        // Verifica se o e-mail já está cadastrado
        if (usuarioRepository.findByEmail(createUserDto.email()).isPresent()) {
            throw new RuntimeException("E-mail já cadastrado");
        }
        if (createUserDto.name().isEmpty() || createUserDto.name().isBlank() || createUserDto.name() == null) {
            throw new RuntimeException("name esta incorreto");
        }
        if (createUserDto.email().isEmpty() || createUserDto.email().isBlank() || createUserDto.email() == null) {
            throw new RuntimeException("email esta incorreto");
        }



        // Define a área do novo usuário: se não vier, usa SEM_AREA
        AreasEnum areaEnum = createUserDto.areas() != null
                ? createUserDto.areas()
                : AreasEnum.SEM_AREA;

        // Cria o novo usuário
        Usuario newUser = Usuario.builder()
                .nome(createUserDto.name())
                .email(createUserDto.email())
                .senha(securityConfiguration.passwordEncoder().encode(createUserDto.password()))
                .roles(List.of(Role.builder().name(RoleName.ROLE_JOGADOR).build()))
                .area(areaEnum.name())
                .build();

        // Salva o novo usuário
        usuarioRepository.save(newUser);

    }


    public ResponseEntity<?> mudarDadosUsuario(String authorizationHeader, UserResponseDto changeUser) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token ausente ou inválido");
        }
        if (usuarioRepository.findByEmail(changeUser.email()).isPresent()) {
            throw new RuntimeException("E-mail já cadastrado");
        }
        if (changeUser.name().isEmpty() || changeUser.name().isBlank() || changeUser.name() == null) {
            throw new RuntimeException("name esta incorreto");
        }
        if (changeUser.email().isEmpty() || changeUser.email().isBlank() || changeUser.email() == null) {
            throw new RuntimeException("email esta incorreto");
        }

        try {
            String token = authorizationHeader.substring(7);
            String email = jwtTokenService.getSubjectFromToken(token);

            Usuario usuario = usuarioRepository.findByEmail(email)
                    .orElseThrow(() -> new DadosInvalidosException("Usuário não encontrado"));

            // Atualiza nome, se fornecido
            if (changeUser.name() != null && !changeUser.name().isBlank()) {
                usuario.setNome(changeUser.name());
            }

            // Atualiza email, se fornecido, e verifica se não está em uso
            if (changeUser.email() != null && !changeUser.email().equals(usuario.getEmail())) {
                if (usuarioRepository.findByEmail(changeUser.email()).isPresent()) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("E-mail já está em uso");
                }
                usuario.setEmail(changeUser.email());
            }

            // Atualiza roles, se fornecidas
            if (changeUser.roles() != null && !changeUser.roles().isEmpty()) {
                List<Role> novasRoles = changeUser.roles().stream()
                        .map(roleName -> roleRepository.findByName(RoleName.valueOf(roleName))
                                .orElseThrow(() -> new DadosInvalidosException("Role não encontrada: " + roleName)))
                        .toList();
                usuario.setRoles(novasRoles);
            }

            // Atualiza área, se fornecida
            if (changeUser.areas() != null) {
                try {
                    AreasEnum areaEnum = AreasEnum.valueOf(changeUser.areas());
                    usuario.setArea(areaEnum.name());
                } catch (IllegalArgumentException e) {
                    return ResponseEntity.badRequest().body("Área inválida: " + changeUser.areas());
                }
            }

            Usuario usuarioAtualizado = usuarioRepository.save(usuario);
            return ResponseEntity.ok(usuarioAtualizado);

        } catch (DadosInvalidosException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar usuário");
        }
    }


    public ResponseEntity<?> dadosUsuario(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token ausente ou inválido");
        }

        try {
            String token = authorizationHeader.substring(7);
            String email = jwtTokenService.getSubjectFromToken(token);

            Usuario usuario = usuarioRepository.findByEmail(email)
                    .orElseThrow(() -> new DadosInvalidosException("Usuário não encontrado"));

            List<String> roles = usuario.getRoles().stream()
                    .map(role -> role.getName().name())
                    .toList();

            UserResponseDto dto = new UserResponseDto(
                    usuario.getNome(),
                    usuario.getEmail(),
                    usuario.getArea(),
                    roles
            );

            return ResponseEntity.ok(dto);

        } catch (DadosInvalidosException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao buscar dados do usuário");
        }
    }

}
