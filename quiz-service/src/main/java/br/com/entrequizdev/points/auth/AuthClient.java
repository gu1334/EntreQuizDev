package br.com.entrequizdev.points.auth;

import br.com.entrequizdev.points.dto.TokenValidationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user-service", url = "http://localhost:8081")
public interface AuthClient {
    @GetMapping("/auth/pergunta")
    ResponseEntity<TokenValidationResponse> validateToken(@RequestHeader("Authorization") String token);
}

