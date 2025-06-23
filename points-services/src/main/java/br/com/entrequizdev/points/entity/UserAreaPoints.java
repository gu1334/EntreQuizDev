package br.com.entrequizdev.pointsservice.entity; // Ajuste o pacote conforme a estrutura do seu projeto

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_area_points") // Nome da tabela no banco de dados
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserAreaPoints {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId; // ID do usuário (vindo do user-service)

    @Column(name = "area_name", nullable = false)
    private String areaName;

    @Column(name = "correct_answers", nullable = false)
    private int correctAnswers;

    @Column(name = "incorrect_answers", nullable = false)
    private int incorrectAnswers;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

}