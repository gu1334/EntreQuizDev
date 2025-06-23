package br.com.entrequizdev.points.entity;

import br.com.entrequizdev.points.enums.AreasEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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
    private Long id;

    @Column(name = "pergunta", nullable = false, length = 500)
    private String pergunta;

    @Column(name = "resposta_correta", nullable = false, length = 255)
    private String respostaCorreta;

    @OneToMany(mappedBy = "pergunta", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<RespostaIncorreta> respostasIncorretas = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "area", nullable = false)
    private AreasEnum area;

    public void addRespostaIncorreta(RespostaIncorreta respostaIncorreta) {
        this.respostasIncorretas.add(respostaIncorreta);
        respostaIncorreta.setPergunta(this);
    }

    public void removeRespostaIncorreta(RespostaIncorreta respostaIncorreta) {
        this.respostasIncorretas.remove(respostaIncorreta);
        respostaIncorreta.setPergunta(null);
    }
}