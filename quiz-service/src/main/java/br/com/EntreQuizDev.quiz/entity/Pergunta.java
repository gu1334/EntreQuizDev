package br.com.EntreQuizDev.quiz.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "perguntas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pergunta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "pergunta")
    private String pergunta;

    @Column(name = "resposta_certa")
    private String resposta_certa;

    @Column(name = "resposta_errada_1")
    private String resposta_errada_1;

    @Column(name = "resposta_errada_2")
    private String resposta_errada_2;

    @Column(name = "resposta_errada_3")
    private String resposta_errada_3;

    @Column(name = "resposta_errada_4")
    private String resposta_errada_4;

    @Column(name = "resposta_errada_5")
    private String resposta_errada_5;

    @Column(name = "resposta_errada_6")
    private String resposta_errada_6;

    @Column(name = "resposta_errada_7")
    private String resposta_errada_7;
}
