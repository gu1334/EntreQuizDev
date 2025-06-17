package br.com.entrequizdev.quiz.dto;

import br.com.entrequizdev.quiz.enums.AreasEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PerguntaDTO {

    private Long id;
    private String pergunta;
    private String respostaCorreta;
    private List<String> respostasIncorretas;
    private AreasEnum area;

}