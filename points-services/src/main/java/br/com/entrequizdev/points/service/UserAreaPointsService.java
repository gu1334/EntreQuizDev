package br.com.entrequizdev.points.service;

import br.com.entrequizdev.points.entity.UserAreaPoints;
import br.com.entrequizdev.points.repository.UserAreaPointsRepository;
import br.com.entrequizdev.shared.dto.UpdatePointsRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime; // Importar LocalDateTime

@Service
public class UserAreaPointsService {

    @Autowired
    private UserAreaPointsRepository userAreaPointsRepository;

    public void criarAtualizarPontuacao (UpdatePointsRequestDTO dto ){
        UserAreaPoints userAreaPoints = userAreaPointsRepository.findByUserIdAndAreaName(dto.getUserId(), dto.getAreaName()).orElse(null);

        if (userAreaPoints == null) {
            userAreaPoints = new UserAreaPoints();
            userAreaPoints.setUserId(dto.getUserId());
            userAreaPoints.setAreaName(dto.getAreaName());
            userAreaPoints.setPoints(dto.getPontosGanhos());
            userAreaPoints.setCorrectAnswers(dto.getAcertosDaSessao());
            userAreaPoints.setIncorrectAnswers(dto.getErrosDaSessao());
        } else {
            userAreaPoints.setCorrectAnswers(userAreaPoints.getCorrectAnswers() + dto.getAcertosDaSessao());
            userAreaPoints.setIncorrectAnswers(userAreaPoints.getIncorrectAnswers() + dto.getErrosDaSessao());
            userAreaPoints.setPoints(userAreaPoints.getPoints() + dto.getPontosGanhos());
        }

        // É importante sempre atualizar a data de última modificação
        userAreaPoints.setLastUpdated(LocalDateTime.now());
        // Salva a entidade (cria ou atualiza) no banco de dados
        userAreaPointsRepository.save(userAreaPoints);
    }


    @KafkaListener(topics = "quiz-points-topic", groupId = "points-service-group")
    public void listenForPointsUpdate(UpdatePointsRequestDTO dto) {
        System.out.println("Mensagem de pontuação recebida do Kafka: " + dto.getUserId() + " - " + dto.getAreaName() + " - " + dto.getPontosGanhos());
        // Chama a lógica principal de atualização de pontos
        criarAtualizarPontuacao(dto);
    }
}