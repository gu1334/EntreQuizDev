package br.com.entrequizdev.quiz.entity;

import br.com.entrequizdev.quiz.enums.AreasEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList; // Importe ArrayList
import java.util.List; // Importe List

@Entity
@Table(name = "perguntas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pergunta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id; // Usar Long para ID é uma boa prática

    @Column(name = "pergunta", nullable = false, length = 500) // Defina um tamanho adequado
    private String pergunta;

    @Column(name = "resposta_correta", nullable = false, length = 255) // Defina um tamanho adequado
    private String respostaCorreta; // Nome da coluna e atributo padronizados

    // Relacionamento One-to-Many com RespostaIncorreta
    // mappedBy indica o campo na entidade RespostaIncorreta que mapeia este relacionamento
    // cascade = CascadeType.ALL significa que operações (persist, remove) na Pergunta
    // serão propagadas para as RespostasIncorretas associadas.
    // orphanRemoval = true garante que se uma resposta incorreta for removida da lista,
    // ela será deletada do banco de dados.
    @OneToMany(mappedBy = "pergunta", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<RespostaIncorreta> respostasIncorretas = new ArrayList<>(); // Inicialize a lista

    @Enumerated(EnumType.STRING)
    @Column(name = "area", nullable = false)
    private AreasEnum area;

    // Método auxiliar para adicionar respostas incorretas e manter a consistência do relacionamento
    public void addRespostaIncorreta(RespostaIncorreta respostaIncorreta) {
        this.respostasIncorretas.add(respostaIncorreta);
        respostaIncorreta.setPergunta(this);
    }

    // Método auxiliar para remover respostas incorretas
    public void removeRespostaIncorreta(RespostaIncorreta respostaIncorreta) {
        this.respostasIncorretas.remove(respostaIncorreta);
        respostaIncorreta.setPergunta(null);
    }
}