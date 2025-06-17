package br.com.entrequizdev.quiz.controller;

import br.com.entrequizdev.quiz.dto.AleatorioRespostasDTO;
import br.com.entrequizdev.quiz.enums.AreasEnum;
import br.com.entrequizdev.quiz.service.RespostaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/resposta")
@Tag(name = "Respostas do Quiz", description = "Endpoints para responder perguntas e buscar perguntas aleatórias.")
public class RespostaController {

    @Autowired
    private RespostaService respostaService;

    @Operation(summary = "Responde uma pergunta",
            description = "Submete uma resposta para uma pergunta específica pelo seu ID e o número da resposta.",
            parameters = {
                    @Parameter(name = "id", description = "ID da pergunta", required = true),
                    @Parameter(name = "numeroResposta", description = "Número da resposta escolhida (ex: 1, 2, 3 ou 4)", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resposta processada com sucesso. Retorna se a resposta está correta ou feedback.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))), // Assumindo que o retorno é uma String de feedback
                    @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos ou pergunta/resposta não encontrada")
            })
    @PostMapping("/{id}/{numeroResposta}")
    public ResponseEntity<?> responderPerguntaPorId(@PathVariable("id") Long id, @PathVariable("numeroResposta") int numeroResposta) {
        return respostaService.responderPergunta(id, numeroResposta);
    }

    @Operation(summary = "Busca uma pergunta aleatória por área",
            description = "Retorna uma pergunta aleatória de uma área específica para o quiz.",
            parameters = {
                    @Parameter(name = "area", description = "Área da pergunta (ex: 'GEOGRAFIA', 'HISTORIA')", required = true, schema = @Schema(implementation = AreasEnum.class))
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Pergunta aleatória retornada com sucesso",
                            content = @Content(schema = @Schema(implementation = AleatorioRespostasDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Nenhuma pergunta encontrada para a área especificada")
            })
    @GetMapping("/aleatoria/{area}")
    public ResponseEntity<AleatorioRespostasDTO> buscarPerguntaPorAreaAleatoria(@PathVariable("area") AreasEnum area) {
        AleatorioRespostasDTO pergunta = respostaService.buscarPerguntaPorAreaAleatorio(area);
        return ResponseEntity.ok(pergunta);
    }
}