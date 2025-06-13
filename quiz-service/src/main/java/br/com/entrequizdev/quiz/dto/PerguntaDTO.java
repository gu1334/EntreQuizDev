package br.com.entrequizdev.quiz.dto;

import br.com.entrequizdev.quiz.enums.AreasEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List; // Importar java.util.List

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PerguntaDTO {

    private Long id;
    private String pergunta;
    private String respostaCorreta; // Alterado para camelCase para consistência
    private List<String> respostasIncorretas; // Usando uma lista para as respostas incorretas
    private AreasEnum area; // Alterado para singular para consistência

}