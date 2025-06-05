package br.com.entrequizdev.user.service;

import br.com.entrequizdev.user.config.SecurityConfiguration;
import br.com.entrequizdev.user.config.UserDetailsImpl;
import br.com.entrequizdev.user.dto.ChangeUser;
import br.com.entrequizdev.user.dto.CreateUserDto;
import br.com.entrequizdev.user.dto.LoginUserDto;
import br.com.entrequizdev.user.dto.RecoveryJwtTokenDto;
import br.com.entrequizdev.user.entity.Role;
import br.com.entrequizdev.user.entity.Usuario;
import br.com.entrequizdev.user.enums.RoleName;
import br.com.entrequizdev.user.exception.DadosInvalidosException;
import br.com.entrequizdev.user.repository.RoleRepository;
import br.com.entrequizdev.user.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
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


        if (usuarioRepository.findByEmail(createUserDto.email()).isPresent()) {

            throw new RuntimeException("email ja cadastrado");
        }
        // Cria um novo usuário com os dados fornecidos
        //caso queira mudar a role, tem que fazer um patch
        Usuario newUser = Usuario.builder()
                .nome(createUserDto.name())
                .email(createUserDto.email())
                // Codifica a senha do usuário com o algoritmo bcrypt
                .senha(securityConfiguration.passwordEncoder().encode(createUserDto.password()))
                // Atribui ao usuário uma permissão específica
                .roles(List.of(Role.builder().name(RoleName.ROLE_JOGADOR).build()))
                .build();


        // Salva o novo usuário no banco de dados
        usuarioRepository.save(newUser);


    }


    public Usuario mudarDadosUsuario(String email, ChangeUser changeUser) {
        Usuario usuarioAntigo = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new DadosInvalidosException("Usuário não encontrado"));

        if (changeUser.name() != null) {
            usuarioAntigo.setNome(changeUser.name());
        }

        if (changeUser.email() != null) {
            usuarioAntigo.setEmail(changeUser.email());
        }

        if (changeUser.password() != null) {
            usuarioAntigo.setSenha(changeUser.password());
        }

        if (changeUser.roleName() != null) {
            Role role = roleRepository.findByName(changeUser.roleName())
                    .orElseThrow(() -> new DadosInvalidosException("Role não encontrada"));

            usuarioAntigo.setRoles(List.of(role)); // substitui todas as roles por essa nova
        }

        return usuarioRepository.save(usuarioAntigo);
    }
}