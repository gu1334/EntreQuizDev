package br.com.entrequizdev.quiz.entity;

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

    @Column(name = "texto_resposta", nullable = false, length = 255) // Ou TEXT se as respostas forem muito longas
    private String textoResposta;

    @ManyToOne // Muitas respostas incorretas podem pertencer a uma pergunta
    @JoinColumn(name = "pergunta_id", nullable = false) // Coluna de chave estrangeira
    private Pergunta pergunta; // Relacionamento com a entidade Pergunta
}