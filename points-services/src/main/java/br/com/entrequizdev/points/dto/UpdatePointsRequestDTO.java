package br.com.entrequizdev.points.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdatePointsRequestDTO {

    private Long userId;
    private String areaName;
    private int pontosGanhos;
    private int acertosDaSessao;
    private int errosDaSessao;
}
