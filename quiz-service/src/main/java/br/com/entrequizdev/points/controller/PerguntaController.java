package br.com.entrequizdev.points.controller;

import br.com.entrequizdev.points.dto.AtualizarPerguntaDTO;
import br.com.entrequizdev.points.dto.PerguntaDTO;
import br.com.entrequizdev.points.service.PerguntaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/perguntas")
@Tag(name = "Perguntas", description = "Gerenciamento de perguntas do quiz (CRUD).")
public class PerguntaController {

    @Autowired
    private PerguntaService perguntaService;

    @Operation(summary = "Cria ou atualiza uma pergunta",
            description = "Cria uma nova pergunta ou atualiza uma existente se o ID for fornecido.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Pergunta criada ou atualizada com sucesso",
                            content = @Content(schema = @Schema(implementation = PerguntaDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos para a pergunta")
            })
    @PostMapping
    public ResponseEntity<PerguntaDTO> criarOuAtualizarPergunta(@RequestBody PerguntaDTO perguntaDTO) {
        PerguntaDTO savedPergunta = perguntaService.createOrUpdateQuestion(perguntaDTO);
        return ResponseEntity.ok(savedPergunta);
    }

    @Operation(summary = "Atualiza parcialmente uma pergunta",
            description = "Atualiza campos específicos de uma pergunta existente pelo ID.",
            parameters = {
                    @Parameter(name = "id", description = "ID da pergunta a ser atualizada", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Pergunta atualizada com sucesso",
                            content = @Content(schema = @Schema(implementation = PerguntaDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Pergunta não encontrada"),
                    @ApiResponse(responseCode = "400", description = "Dados de atualização inválidos")
            })
    @PatchMapping("/{id}")
    public ResponseEntity<PerguntaDTO> atualizarPergunta(@PathVariable("id") Long id, @RequestBody AtualizarPerguntaDTO atualizarPerguntaDTO) {
        PerguntaDTO updatedPergunta = perguntaService.atualizarPergunta(id, atualizarPerguntaDTO);
        return ResponseEntity.ok(updatedPergunta);
    }

    @Operation(summary = "Busca todas as perguntas",
            description = "Retorna uma lista de todas as perguntas cadastradas no sistema.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de perguntas retornada com sucesso",
                            content = @Content(schema = @Schema(implementation = PerguntaDTO.class))) // Para listas, o schema é o tipo do elemento da lista
            })
    @GetMapping
    public ResponseEntity<List<PerguntaDTO>> buscarTodasPerguntas() {
        List<PerguntaDTO> perguntas = perguntaService.buscarTodasPerguntas();
        return ResponseEntity.ok(perguntas);
    }

    @Operation(summary = "Deleta uma pergunta",
            description = "Exclui uma pergunta do sistema pelo seu ID.",
            parameters = {
                    @Parameter(name = "id", description = "ID da pergunta a ser deletada", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "204", description = "Pergunta deletada com sucesso (No Content)"),
                    @ApiResponse(responseCode = "404", description = "Pergunta não encontrada")
            })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarPergunta(@PathVariable Long id) {
        perguntaService.deletarPergunta(id);
    }
}