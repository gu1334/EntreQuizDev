package br.com.EntreQuizDev.quiz.repository;

import br.com.EntreQuizDev.quiz.entity.Pergunta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerguntaRepository extends JpaRepository<Pergunta, Long> {

}
