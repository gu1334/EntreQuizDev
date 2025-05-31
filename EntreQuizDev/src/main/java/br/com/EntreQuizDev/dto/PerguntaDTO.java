package br.com.EntreQuizDev.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PerguntaDTO {

    private Long id;
    private String pergunta;
    private String resposta_certa;
    private String resposta_errada_1;
    private String resposta_errada_2;
    private String resposta_errada_3;
    private String resposta_errada_4;
    private String resposta_errada_5;
    private String resposta_errada_6;
    private String resposta_errada_7;

}
