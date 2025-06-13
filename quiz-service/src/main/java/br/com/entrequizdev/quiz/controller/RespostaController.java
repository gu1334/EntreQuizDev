package br.com.entrequizdev.quiz.controller;

import br.com.entrequizdev.quiz.dto.AleatorioRespostasDTO;
import br.com.entrequizdev.quiz.dto.PerguntaDTO;
import br.com.entrequizdev.quiz.enums.AreasEnum;
import br.com.entrequizdev.quiz.service.RespostaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/resposta")
public class RespostaController {

    @Autowired
    private RespostaService respostaService;


    @PostMapping("/{id}/{numeroResposta}")
    public ResponseEntity<?> responderPerguntaPorId(@PathVariable("id") Long id, @PathVariable("numeroResposta") int numeroResposta) {
        return respostaService.responderPergunta(id, numeroResposta);
    }

    @GetMapping("/aleatoria/{area}") // Endpoint mais descritivo
    public ResponseEntity<AleatorioRespostasDTO> buscarPerguntaPorAreaAleatoria(@PathVariable("area") AreasEnum area) {
        AleatorioRespostasDTO pergunta = respostaService.buscarPerguntaPorAreaAleatorio(area);
        return ResponseEntity.ok(pergunta);
    }
}
