package br.com.entrequizdev.quiz.repository;

import br.com.entrequizdev.quiz.entity.Pergunta;
import br.com.entrequizdev.quiz.enums.AreasEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PerguntaRepository extends JpaRepository<Pergunta, Long> {


    List<Pergunta> findPerguntasByArea(AreasEnum area);
}
