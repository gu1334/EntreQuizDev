package br.com.EntreQuizDev.repository;

import br.com.EntreQuizDev.entity.Pergunta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerguntaRepository extends JpaRepository<Pergunta, Long> {

}
