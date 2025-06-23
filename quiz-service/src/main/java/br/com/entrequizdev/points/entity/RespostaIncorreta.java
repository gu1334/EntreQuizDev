package br.com.entrequizdev.points.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "respostas_incorretas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RespostaIncorreta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "texto_resposta", nullable = false, length = 255)
    private String textoResposta;

    @ManyToOne
    @JoinColumn(name = "pergunta_id", nullable = false)
    private Pergunta pergunta;
}