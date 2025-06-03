package br.com.EntreQuizDev.user.service;

import br.com.EntreQuizDev.user.config.SecurityConfiguration;
import br.com.EntreQuizDev.user.config.UserDetailsImpl;
import br.com.EntreQuizDev.user.dto.CreateUserDto;
import br.com.EntreQuizDev.user.dto.LoginUserDto;
import br.com.EntreQuizDev.user.dto.RecoveryJwtTokenDto;
import br.com.EntreQuizDev.user.entity.Role;
import br.com.EntreQuizDev.user.entity.Usuario;
import br.com.EntreQuizDev.user.enums.RoleName;
import br.com.EntreQuizDev.user.repository.UsuarioRepository;
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


}