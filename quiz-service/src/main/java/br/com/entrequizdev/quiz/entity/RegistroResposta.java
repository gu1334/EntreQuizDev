package br.com.entrequizdev.quiz.entity;

import br.com.entrequizdev.quiz.entity.Pergunta;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "registros_respostas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistroResposta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pergunta_id", nullable = false)
    private Pergunta pergunta;

    @Column(name = "resposta_escolhida", nullable = false, length = 255)
    private String respostaEscolhida;

    @Column(name = "is_correta", nullable = false)
    private boolean correta;

    @Column(name = "data_hora_resposta", nullable = false)
    private LocalDateTime dataHoraResposta;

}