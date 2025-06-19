package br.com.entrequizdev.quiz.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "points")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PointsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "acertos")
    private int acertos;

    @Column(name = "erros")
    private int erros;

    
}
