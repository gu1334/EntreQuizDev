package br.com.entrequizdev.quiz.dto;

import br.com.entrequizdev.quiz.enums.AreasEnum;

public record AtualizarPergunta(
        String pergunta,
        String resposta_certa,
        String resposta_errada_1,
        String resposta_errada_2,
        String resposta_errada_3,
        String resposta_errada_4,
        String resposta_errada_5,
        String resposta_errada_6,
        String resposta_errada_7,
        AreasEnum areas
        ) {
}
