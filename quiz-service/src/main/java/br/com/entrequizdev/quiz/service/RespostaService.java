package br.com.entrequizdev.quiz.service;

import br.com.entrequizdev.quiz.dto.AleatorioRespostasDTO;
import br.com.entrequizdev.quiz.dto.PerguntaDTO;
import br.com.entrequizdev.quiz.entity.Pergunta;
import br.com.entrequizdev.quiz.entity.RegistroResposta; // Importe a nova entidade
import br.com.entrequizdev.quiz.entity.RespostaIncorreta;
import br.com.entrequizdev.quiz.enums.AreasEnum;
import br.com.entrequizdev.quiz.repository.PerguntaRepository;
import br.com.entrequizdev.quiz.repository.RegistroRespostaRepository; // Importe o novo repositório
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime; // Importe para usar LocalDateTime
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RespostaService {

    @Autowired
    private PerguntaRepository perguntaRepository;

    @Autowired
    private RegistroRespostaRepository registroRespostaRepository; // <--- NOVIDADE AQUI: Injetando o novo repositório

    // ... (Seus métodos toDTO e aleatorioRespostasDTO permanecem os mesmos) ...
    private PerguntaDTO toDTO(Pergunta entity) {
        PerguntaDTO dto = new PerguntaDTO();
        dto.setId(entity.getId());
        dto.setPergunta(entity.getPergunta());
        dto.setRespostaCorreta(entity.getRespostaCorreta());

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
        todasAsRespostas.add(entity.getRespostaCorreta());

        if (entity.getRespostasIncorretas() != null) {
            todasAsRespostas.addAll(entity.getRespostasIncorretas().stream()
                    .map(RespostaIncorreta::getTextoResposta)
                    .collect(Collectors.toList()));
        }

        Collections.shuffle(todasAsRespostas);
        dto.setRespostas(todasAsRespostas);

        return dto;
    }

    public AleatorioRespostasDTO buscarPerguntaPorAreaAleatorio(AreasEnum area) {
        List<Pergunta> todasPerguntas = perguntaRepository.findPerguntasByArea(area);

        if (todasPerguntas.isEmpty()) {
            throw new EntityNotFoundException("Não há perguntas para a área: " + area.name());
        }

        int indiceAleatorio = (int) (Math.random() * todasPerguntas.size());
        Pergunta perguntaAleatoria = todasPerguntas.get(indiceAleatorio);

        return aleatorioRespostasDTO(perguntaAleatoria);
    }

    @SuppressWarnings("checkstyle:LineLength") // Ignora o erro de tamanho da linha para a mensagem de sucesso
    public ResponseEntity<?> responderPergunta(Long id, int numeroResposta) {
        Pergunta pergunta = perguntaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pergunta com ID " + id + " não encontrada."));

        List<String> todasRespostas = new ArrayList<>();
        todasRespostas.add(pergunta.getRespostaCorreta());
        pergunta.getRespostasIncorretas().forEach(ri -> todasRespostas.add(ri.getTextoResposta()));

        if (numeroResposta < 0 || numeroResposta >= todasRespostas.size()) {
            return ResponseEntity.badRequest().body("Número de resposta inválido.");
        }

        String respostaEscolhida = todasRespostas.get(numeroResposta);
        boolean isCorreta = pergunta.getRespostaCorreta().equalsIgnoreCase(respostaEscolhida);

        // <--- NOVIDADE AQUI: Salvando o registro da resposta
        RegistroResposta registro = new RegistroResposta();
        registro.setPergunta(pergunta); // Associa a pergunta que foi respondida
        registro.setRespostaEscolhida(respostaEscolhida); // Salva o texto da resposta que o usuário escolheu
        registro.setCorreta(isCorreta); // Define se foi correta ou incorreta
        registro.setDataHoraResposta(LocalDateTime.now()); // Registra o momento da resposta

        registroRespostaRepository.save(registro); // Salva o registro no banco de dados

        // Retorna a resposta para o front-end
        if (isCorreta) {
            return ResponseEntity.ok("Resposta Correta! Registro salvo.");
        } else {
            return ResponseEntity.ok("Resposta Incorreta. Registro salvo.");
        }
    }
}