package br.com.entrequizdev.quiz.repository;

import br.com.entrequizdev.quiz.entity.Pergunta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerguntaRepository extends JpaRepository<Pergunta, Long> {

}
