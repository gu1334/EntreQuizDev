package br.com.entrequizdev.quiz.controller;

import br.com.entrequizdev.quiz.dto.AtualizarPerguntaDTO;
import br.com.entrequizdev.quiz.dto.PerguntaDTO;
import br.com.entrequizdev.quiz.entity.Pergunta;
import br.com.entrequizdev.quiz.service.PerguntaService;
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


    @PostMapping
    public ResponseEntity<?> criarOuAtualizarPergunta(@RequestBody PerguntaDTO perguntaDTO) {

        perguntaService.createOrUptadeQuestion(perguntaDTO);
        return ResponseEntity.noContent().build();

    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> atualizarPergunta(@PathVariable("id") Long id, @RequestBody AtualizarPerguntaDTO atualizarPerguntaDTO) {


        perguntaService.atualizarPergunta(id, atualizarPerguntaDTO);
        return ResponseEntity.noContent().build();

    }

    @GetMapping
    public List<Pergunta> buscarTodasPerguntas() {
        return perguntaService.buscarTodasPerguntas();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarPergunta(@PathVariable Long id) {
        perguntaService.deletarPergunta(id);
    }

}
