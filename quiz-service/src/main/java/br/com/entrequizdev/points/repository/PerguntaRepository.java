package br.com.entrequizdev.points.repository;

import br.com.entrequizdev.points.entity.Pergunta;
import br.com.entrequizdev.points.enums.AreasEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PerguntaRepository extends JpaRepository<Pergunta, Long> {


    List<Pergunta> findPerguntasByArea(AreasEnum area);
}
