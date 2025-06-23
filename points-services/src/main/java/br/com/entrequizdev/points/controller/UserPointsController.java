package br.com.entrequizdev.points.controller;

import br.com.entrequizdev.points.dto.UpdatePointsRequestDTO;
import br.com.entrequizdev.points.service.UserAreaPointsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/points")
@Tag(name = "pontos", description = "Crud de pontos")
public class UserPointsController {

    @Autowired
    private UserAreaPointsService service;

    @PostMapping
    public ResponseEntity<Void> criarOuAtualizarPontuacao (@RequestBody UpdatePointsRequestDTO dto) {
        service.criarAtualizarPontuacao(dto);
        return ResponseEntity.ok().build();
    }


}
