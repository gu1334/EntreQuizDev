package br.com.entrequizdev.quiz.controller;

import br.com.entrequizdev.quiz.dto.AtualizarPergunta;
import br.com.entrequizdev.quiz.dto.PerguntaDTO;
import br.com.entrequizdev.quiz.entity.Pergunta;
import br.com.entrequizdev.quiz.service.PerguntaService;
import com.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/CadastrarPergunta")
public class PerguntaController {

    @Autowired
    private PerguntaService perguntaService;

    @Autowired
    private JwtUtil jwtUtil;  // injeta o JwtUtil para validar token

    private boolean validarToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return false;
        }
        String token = authHeader.substring(7);  // remove "Bearer "
        return jwtUtil.validateToken(token);
    }

    @PostMapping
    public ResponseEntity<?> criarOuAtualizarPergunta(@RequestHeader(value = "Authorization", required = false) String authHeader,
                                                      @RequestBody PerguntaDTO perguntaDTO) {

        if (!validarToken(authHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        perguntaService.createOrUptadeQuestion(perguntaDTO);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> atualizarPergunta(@RequestHeader(value = "Authorization", required = false) String authHeader,
                                               @PathVariable("id") Long id,
                                               @RequestBody AtualizarPergunta atualizarPergunta) {

        if (!validarToken(authHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        perguntaService.atualizarPergunta(id, atualizarPergunta);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Pergunta>> buscarTodasPerguntas(@RequestHeader(value = "Authorization", required = false) String authHeader) {

        if (!validarToken(authHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<Pergunta> perguntas = perguntaService.buscarTodasPerguntas();
        return ResponseEntity.ok(perguntas);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarPergunta(@RequestHeader(value = "Authorization", required = false) String authHeader,
                                             @PathVariable Long id) {

        if (!validarToken(authHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        perguntaService.deletarPergunta(id);
        return ResponseEntity.noContent().build();
    }
}
