package br.com.entrequizdev.user.controller;

import br.com.entrequizdev.user.service.JwtTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtTokenService jwtTokenService;

    // Endpoint para validar token (GET)
    @GetMapping("/pergunta")
    public ResponseEntity<String> validateToken(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token não fornecido");
            }

            String token = authorizationHeader.substring(7); // remove "Bearer "
            String subject = jwtTokenService.getSubjectFromToken(token);

            // Se chegou até aqui, token é válido
            return ResponseEntity.ok("Token válido para usuário: " + subject);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido ou expirado");
        }
    }
}
