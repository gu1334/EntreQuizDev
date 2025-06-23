package br.com.entrequizdev.points.repository;// package br.com.entrequizdev.quiz.repository; // Certifique-se de que o pacote está correto

import br.com.entrequizdev.points.entity.RegistroResposta; // Importe a sua nova entidade
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistroRespostaRepository extends JpaRepository<RegistroResposta, Long> {

}