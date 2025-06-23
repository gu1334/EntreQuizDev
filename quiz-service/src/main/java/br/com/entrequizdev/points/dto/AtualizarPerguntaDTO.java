package br.com.entrequizdev.points.dto;

import br.com.entrequizdev.points.enums.AreasEnum;

import java.util.List; // Importar java.util.List

public record AtualizarPerguntaDTO(
        String pergunta,
        String respostaCorreta, // Alterado para camelCase para consistência
        List<String> respostasIncorretas, // Usando uma lista para as respostas incorretas
        AreasEnum area // Alterado para singular para consistência
) {
}