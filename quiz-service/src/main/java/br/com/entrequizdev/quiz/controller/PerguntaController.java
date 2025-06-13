package br.com.entrequizdev.quiz.controller;

import br.com.entrequizdev.quiz.dto.AtualizarPerguntaDTO;
import br.com.entrequizdev.quiz.dto.PerguntaDTO;
import br.com.entrequizdev.quiz.enums.AreasEnum;
import br.com.entrequizdev.quiz.service.PerguntaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/perguntas") // Geralmente o nome do endpoint é plural e descritivo
public class PerguntaController {

    @Autowired
    private PerguntaService perguntaService;


    @PostMapping
    public ResponseEntity<PerguntaDTO> criarOuAtualizarPergunta(@RequestBody PerguntaDTO perguntaDTO) {
        // O serviço já lida com a lógica de criar ou atualizar.
        // Se o ID for nulo, cria; se existir, atualiza.
        PerguntaDTO savedPergunta = perguntaService.createOrUpdateQuestion(perguntaDTO);
        // Retornamos 200 OK e a pergunta salva/atualizada.
        return ResponseEntity.ok(savedPergunta);
    }


    @PatchMapping("/{id}")
    public ResponseEntity<PerguntaDTO> atualizarPergunta(@PathVariable("id") Long id, @RequestBody AtualizarPerguntaDTO atualizarPerguntaDTO) {
        PerguntaDTO updatedPergunta = perguntaService.atualizarPergunta(id, atualizarPerguntaDTO);
        return ResponseEntity.ok(updatedPergunta);
    }


    @GetMapping
    public ResponseEntity<List<PerguntaDTO>> buscarTodasPerguntas() {
        List<PerguntaDTO> perguntas = perguntaService.buscarTodasPerguntas();
        return ResponseEntity.ok(perguntas);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Retorna 204 NO CONTENT para deleções bem-sucedidas
    public void deletarPergunta(@PathVariable Long id) {
        perguntaService.deletarPergunta(id);
    }


}