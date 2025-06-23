package br.com.entrequizdev.points.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AleatorioRespostasDTO {
    private Long id;
    private String pergunta;
    private List<String> respostas;
}
