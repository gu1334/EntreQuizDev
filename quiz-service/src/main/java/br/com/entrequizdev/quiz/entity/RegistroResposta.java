package br.com.entrequizdev.quiz.entity;// package br.com.entrequizdev.quiz.entity; // Onde você coloca suas entidades

import br.com.entrequizdev.quiz.entity.Pergunta;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime; // Importe para a data e hora

@Entity
@Table(name = "registros_respostas") // Nome da tabela no banco de dados
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistroResposta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relacionamento com a Pergunta
    @ManyToOne
    @JoinColumn(name = "pergunta_id", nullable = false) // Coluna que faz referência à Pergunta
    private Pergunta pergunta;

    @Column(name = "resposta_escolhida", nullable = false, length = 255)
    private String respostaEscolhida;

    @Column(name = "is_correta", nullable = false) // Nome da coluna no banco (is_correta ou apenas correta)
    private boolean correta; // true se a resposta foi correta, false caso contrário

    @Column(name = "data_hora_resposta", nullable = false)
    private LocalDateTime dataHoraResposta;

    // Se tiver um sistema de usuários, você pode adicionar um relacionamento aqui:
    // @ManyToOne
    // @JoinColumn(name = "usuario_id")
    // private Usuario usuario; // Você precisaria criar a classe Usuario também
}