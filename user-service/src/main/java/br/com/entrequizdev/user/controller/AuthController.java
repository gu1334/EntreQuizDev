package br.com.entrequizdev.user.controller;

import br.com.entrequizdev.user.dto.TokenValidationResponse;
import br.com.entrequizdev.user.service.JwtTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "Endpoints para gerenciamento de tokens JWT.") // Tag para agrupar
public class AuthController {

    @Autowired
    private JwtTokenService jwtTokenService;

    @Operation(summary = "Valida um token JWT", // Resumo da operação
            description = "Verifica se um token JWT fornecido no cabeçalho 'Authorization' é válido e ativo.", // Descrição
            parameters = {
                    @Parameter(name = "Authorization", description = "Token JWT no formato 'Bearer [token]'", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Token válido"),
                    @ApiResponse(responseCode = "401", description = "Token não fornecido ou inválido")
            })
    @GetMapping("/pergunta")
    public ResponseEntity<TokenValidationResponse> validateToken(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // Pode retornar null ou um erro customizado
            }

            String token = authorizationHeader.substring(7); // remove "Bearer "

            // Chamar os novos métodos para obter subject e roles
            String subject = jwtTokenService.getSubjectFromToken(token);
            List<String> roles = jwtTokenService.getRolesFromToken(token);

            // Criar o objeto de resposta
            TokenValidationResponse response = new TokenValidationResponse(subject, roles);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Logar a exceção para depuração
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // Pode retornar null ou um erro customizado
        }
    }
}