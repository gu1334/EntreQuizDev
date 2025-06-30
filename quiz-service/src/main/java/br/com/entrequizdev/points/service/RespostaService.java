package br.com.entrequizdev.points.service;

import br.com.entrequizdev.points.dto.AleatorioRespostasDTO;
import br.com.entrequizdev.points.dto.PerguntaDTO;
import br.com.entrequizdev.points.entity.Pergunta;
import br.com.entrequizdev.points.entity.RegistroResposta;
import br.com.entrequizdev.points.entity.RespostaIncorreta;
import br.com.entrequizdev.points.enums.AreasEnum;
import br.com.entrequizdev.points.repository.PerguntaRepository;
import br.com.entrequizdev.points.repository.RegistroRespostaRepository;
import br.com.entrequizdev.shared.dto.UpdatePointsRequestDTO; // Importe o DTO compartilhado
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication; // Importe a classe Authentication
import org.springframework.security.core.context.SecurityContextHolder; // Importe SecurityContextHolder

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RespostaService {

    @Autowired
    private PerguntaRepository perguntaRepository;

    @Autowired
    private RegistroRespostaRepository registroRespostaRepository;

    // Injete o KafkaTemplate através do construtor
    private final KafkaTemplate<String, UpdatePointsRequestDTO> kafkaTemplate;

    public RespostaService(KafkaTemplate<String, UpdatePointsRequestDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

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

    public ResponseEntity<?> responderPergunta(Long id, int numeroResposta) {
        Pergunta pergunta = perguntaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pergunta com ID " + id + " não encontrada."));

        List<String> todasRespostas = new ArrayList<>();
        todasRespostas.add(pergunta.getRespostaCorreta());
        pergunta.getRespostasIncorretas().forEach(ri -> todasRespostas.add(ri.getTextoResposta()));

        int indiceRealDaResposta = numeroResposta - 1;

        if (indiceRealDaResposta < 0 || indiceRealDaResposta >= todasRespostas.size()) {
            return ResponseEntity.badRequest().body("Número de resposta inválido.");
        }

        String respostaEscolhida = todasRespostas.get(indiceRealDaResposta);
        boolean isCorreta = pergunta.getRespostaCorreta().equalsIgnoreCase(respostaEscolhida);

        RegistroResposta registro = new RegistroResposta();
        registro.setPergunta(pergunta);
        registro.setRespostaEscolhida(respostaEscolhida);
        registro.setCorreta(isCorreta);
        registro.setDataHoraResposta(LocalDateTime.now());

        registroRespostaRepository.save(registro);

        // --- LÓGICA PARA OBTER USERID E ENVIAR PARA O KAFKA ---
        Long userId = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof String) {
                try {
                    userId = Long.parseLong((String) principal);
                } catch (NumberFormatException e) {
                    System.err.println("Erro: userId no principal não é um número válido: " + principal);
                    // Considere lançar uma exceção ou retornar um erro HTTP adequado aqui em um ambiente de produção
                }
            }
            // Adicione outras verificações de 'instanceof' se o seu Principal for de outro tipo (ex: UserDetails)
        }

        if (userId == null) {
            // Se o userId não foi encontrado, é um erro grave na autenticação ou configuração.
            // Para produção, você DEVERIA lançar uma exceção ou retornar um erro HTTP (e.g., HttpStatus.UNAUTHORIZED).
            // Para fins de teste, estamos usando um fallback, mas saiba que é temporário.
            System.err.println("ATENÇÃO: userId não encontrado no contexto de segurança. Usando fallback 999L para teste.");
            userId = 999L; // FALLBACK PARA TESTE - REMOVER EM PRODUÇÃO!
        }

        String areaName = pergunta.getArea().name(); // Obtem o nome da área da pergunta

        // Calcula os pontos e acertos/erros para esta resposta específica
        int pontosGanhosPorEstaResposta = isCorreta ? 10 : 0; // Exemplo: 10 pontos por acerto, 0 por erro
        int acertosPorEstaResposta = isCorreta ? 1 : 0;
        int errosPorEstaResposta = isCorreta ? 0 : 1;

        // Chama o método para enviar a mensagem Kafka
        // Este método 'processQuizResult' é o que você definiu para ENVIAR para o Kafka.
        // Ele não faz a lógica de pegar o usuário, pois já recebe esses dados.
        processQuizResult(userId, areaName, pontosGanhosPorEstaResposta, acertosPorEstaResposta, errosPorEstaResposta);

        // Retorna a resposta HTTP original ao cliente
        if (isCorreta) {
            return ResponseEntity.ok("Resposta Correta! Registro salvo e pontos enviados para processamento.");
        } else {
            return ResponseEntity.ok("Resposta Incorreta. Registro salvo e pontos enviados para processamento.");
        }
    }

    // Este método é responsável APENAS por enviar a mensagem para o Kafka
    public void processQuizResult(Long userId, String areaName, int pontosGanhos, int acertosDaSessao, int errosDaSessao) {
        UpdatePointsRequestDTO pointsDto = new UpdatePointsRequestDTO(userId, areaName, pontosGanhos, acertosDaSessao, errosDaSessao);
        String topicName = "quiz-points-topic";
        kafkaTemplate.send(topicName, pointsDto);
        System.out.println("Mensagem de pontuação enviada para o Kafka: UserID=" + pointsDto.getUserId() + ", Area=" + pointsDto.getAreaName() + ", Pontos=" + pointsDto.getPontosGanhos());
    }
}