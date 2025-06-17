package br.com.entrequizdev.user.controller;

import br.com.entrequizdev.user.dto.*;
import br.com.entrequizdev.user.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
@Tag(name = "Usuários", description = "Endpoints para gerenciamento de usuários (cadastro, login, dados e testes de permissão).") // Adiciona tag para o controller
public class UserController {

    @Autowired
    private UsuarioService usuarioService;

    @Operation(summary = "Autentica um usuário",
            description = "Realiza o login de um usuário e retorna um token JWT para acesso futuro.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Credenciais do usuário para login",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoginUserDto.class),
                            examples = @ExampleObject(
                                    value = "{\"email\": \"usuario@example.com\", \"password\": \"senha123\"}"
                            )
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Autenticação bem-sucedida, token JWT retornado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RecoveryJwtTokenDto.class), // <-- CORRIGIDO AQUI
                                    examples = @ExampleObject(value = "{\"token\": \"eyJhbGciOiJIUzI1Ni...\"}"))),
                    @ApiResponse(responseCode = "401", description = "Credenciais inválidas",
                            content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "Credenciais inválidas"))), // <-- CORRIGIDO AQUI
                    @ApiResponse(responseCode = "400", description = "Requisição mal-formatada",
                            content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "Dados de login inválidos"))) // <-- CORRIGIDO AQUI
            })
    @PostMapping("/login")
    public ResponseEntity<RecoveryJwtTokenDto> authenticateUser(@RequestBody LoginUserDto loginUserDto) {
        RecoveryJwtTokenDto token = usuarioService.authenticateUser(loginUserDto);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @Operation(summary = "Cria um novo usuário",
            description = "Registra um novo usuário no sistema. A role padrão pode ser definida internamente ou inferida.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados para criação do usuário",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CreateUserDto.class),
                            examples = @ExampleObject(
                                    value = "{\"email\": \"novo.usuario@example.com\", \"password\": \"novaSenha123\", \"name\": \"Novo Usuário\"}"
                            )
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Dados de criação inválidos ou usuário já existe",
                            content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "E-mail já cadastrado"))) // <-- CORRIGIDO AQUI
            })
    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody CreateUserDto createUserDto) {
        usuarioService.createUser(createUserDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Teste de autenticação geral",
            description = "Endpoint de teste que qualquer usuário autenticado pode acessar.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Autenticação bem-sucedida",
                            content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "Autenticado com sucesso"))), // <-- CORRIGIDO AQUI
                    @ApiResponse(responseCode = "401", description = "Não autenticado")
            })
    @GetMapping("/test")
    public ResponseEntity<String> getAuthenticationTest() {
        return new ResponseEntity<>("Autenticado com sucesso", HttpStatus.OK);
    }

    @Operation(summary = "Teste de autenticação para cliente",
            description = "Endpoint de teste que somente usuários com a role 'JOGADOR' podem acessar.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cliente autenticado com sucesso",
                            content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "Cliente autenticado com sucesso"))), // <-- CORRIGIDO AQUI
                    @ApiResponse(responseCode = "403", description = "Acesso negado (permissão insuficiente)")
            })
    @GetMapping("/test/customer")
    public ResponseEntity<String> getCustomerAuthenticationTest() {
        return new ResponseEntity<>("Cliente autenticado com sucesso", HttpStatus.OK);
    }

    @Operation(summary = "Teste de autenticação para administrador",
            description = "Endpoint de teste que somente usuários com a role 'ADMINISTRATOR' podem acessar.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Administrador autenticado com sucesso",
                            content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "Administrador autenticado com sucesso"))), // <-- CORRIGIDO AQUI
                    @ApiResponse(responseCode = "403", description = "Acesso negado (permissão insuficiente)")
            })
    @GetMapping("/test/administrator")
    public ResponseEntity<String> getAdminAuthenticationTest() {
        return new ResponseEntity<>("Administrador autenticado com sucesso", HttpStatus.OK);
    }

    @Operation(summary = "Atualiza dados do usuário",
            description = "Permite que um usuário autenticado atualize seus próprios dados.",
            parameters = {
                    @Parameter(name = "Authorization", description = "Token JWT no formato 'Bearer [token]'", required = true,
                            schema = @Schema(type = "string", format = "jwt", example = "Bearer eyJhbGciOiJIUzI1Ni..."))
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados do usuário para atualização",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDto.class), // Assume que UserResponseDto é o DTO para atualizar
                            examples = @ExampleObject(
                                    value = "{\"email\": \"novo.email@example.com\", \"name\": \"Nome Atualizado\"}"
                            )
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Dados do usuário atualizados com sucesso"),
                    @ApiResponse(responseCode = "401", description = "Não autenticado ou token inválido"),
                    @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos ou sem permissão")
            })
    @PatchMapping("/preferences")
    public ResponseEntity<?> mudarDadosUser(@RequestHeader("Authorization") String authorizationHeader, @RequestBody UserResponseDto changeUser) {
        usuarioService.mudarDadosUsuario(authorizationHeader, changeUser);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Retorna dados do usuário autenticado",
            description = "Recupera as informações do usuário logado com base no token JWT fornecido.",
            parameters = {
                    @Parameter(name = "Authorization", description = "Token JWT no formato 'Bearer [token]'", required = true,
                            schema = @Schema(type = "string", format = "jwt", example = "Bearer eyJhbGciOiJIUzI1Ni..."))
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Dados do usuário recuperados com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))), // <-- CORRIGIDO AQUI
                    @ApiResponse(responseCode = "401", description = "Não autenticado ou token inválido")
            })
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@RequestHeader("Authorization") String authorizationHeader) {
        return usuarioService.dadosUsuario(authorizationHeader);
    }
}