package br.com.EntreQuizDev.controller;

import br.com.EntreQuizDev.dto.PerguntaDTO;
import br.com.EntreQuizDev.entity.Pergunta;
import br.com.EntreQuizDev.dto.AtualizarPergunta;
import br.com.EntreQuizDev.service.PerguntaService;
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
    public ResponseEntity<?> atualizarPergunta(@PathVariable("id") Long id, @RequestBody AtualizarPergunta atualizarPergunta) {


        perguntaService.atualizarPergunta(id, atualizarPergunta);
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
