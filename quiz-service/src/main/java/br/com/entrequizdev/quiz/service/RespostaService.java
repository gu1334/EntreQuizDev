package br.com.entrequizdev.quiz.service;

import br.com.entrequizdev.quiz.dto.AleatorioRespostasDTO;
import br.com.entrequizdev.quiz.dto.PerguntaDTO;
import br.com.entrequizdev.quiz.entity.Pergunta;
import br.com.entrequizdev.quiz.entity.RespostaIncorreta;
import br.com.entrequizdev.quiz.enums.AreasEnum;
import br.com.entrequizdev.quiz.repository.PerguntaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RespostaService {
    @Autowired
    private PerguntaRepository perguntaRepository;

    private PerguntaDTO toDTO(Pergunta entity) {
        PerguntaDTO dto = new PerguntaDTO();
        dto.setId(entity.getId());
        dto.setPergunta(entity.getPergunta());
        dto.setRespostaCorreta(entity.getRespostaCorreta());

        // Mapear respostas incorretas da entidade para DTO
        if (entity.getRespostasIncorretas() != null) {
            dto.setRespostasIncorretas(entity.getRespostasIncorretas().stream()
                    .map(RespostaIncorreta::getTextoResposta)
                    .collect(Collectors.toList()));
        }
        dto.setArea(entity.getArea());
        return dto;
    }

    private AleatorioRespostasDTO aleatorioRespostasDTO(Pergunta entity) {
        AleatorioRespostasDTO dto = new AleatorioRespostasDTO();
        dto.setId(entity.getId());
        dto.setPergunta(entity.getPergunta());

        List<String> todasAsRespostas = new ArrayList<>();
        // 1. Adiciona a resposta correta
        todasAsRespostas.add(entity.getRespostaCorreta());

        // 2. Adiciona as respostas incorretas (se existirem)
        if (entity.getRespostasIncorretas() != null) {
            todasAsRespostas.addAll(entity.getRespostasIncorretas().stream()
                    .map(RespostaIncorreta::getTextoResposta)
                    .collect(Collectors.toList()));
        }

        // 3. Embaralha a lista de todas as respostas
        Collections.shuffle(todasAsRespostas);

        // Define a lista embaralhada no DTO
        dto.setRespostas(todasAsRespostas);

        return dto;
    }

    public AleatorioRespostasDTO buscarPerguntaPorAreaAleatorio(AreasEnum area) { // Retornar DTO

        List<Pergunta> todasPerguntas = perguntaRepository.findPerguntasByArea(area);

        if (todasPerguntas.isEmpty()) {
            throw new EntityNotFoundException("Não há perguntas para a área: " + area.name());
        }

        int indiceAleatorio = (int) (Math.random() * todasPerguntas.size());

        Pergunta perguntaAleatoria = todasPerguntas.get(indiceAleatorio);

        return aleatorioRespostasDTO(perguntaAleatoria);
    }



    public ResponseEntity<?> responderPergunta(Long id, int numeroResposta) {

        Pergunta pergunta = perguntaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pergunta com ID " + id + " não encontrada."));

        // Lista de todas as respostas (correta + incorretas) para comparar com o númeroResposta
        List<String> todasRespostas = new ArrayList<>();
        todasRespostas.add(pergunta.getRespostaCorreta());
        pergunta.getRespostasIncorretas().forEach(ri -> todasRespostas.add(ri.getTextoResposta()));

        // Valida se o número da resposta está dentro dos limites
        if (numeroResposta < 0 || numeroResposta >= todasRespostas.size()) {
            return ResponseEntity.badRequest().body("Número de resposta inválido.");
        }

        String respostaEscolhida = todasRespostas.get(numeroResposta);

        if (pergunta.getRespostaCorreta().equalsIgnoreCase(respostaEscolhida)) {
            return ResponseEntity.ok("Resposta Correta!");
        } else {
            return ResponseEntity.ok("Resposta Incorreta."); // Ou um JSON com mais detalhes
        }
    }
}
