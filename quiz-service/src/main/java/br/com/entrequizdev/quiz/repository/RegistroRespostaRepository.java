package br.com.entrequizdev.quiz.repository;// package br.com.entrequizdev.quiz.repository; // Certifique-se de que o pacote está correto

import br.com.entrequizdev.quiz.entity.RegistroResposta; // Importe a sua nova entidade
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // Anotação para indicar que é um componente de repositório
public interface RegistroRespostaRepository extends JpaRepository<RegistroResposta, Long> {
    // Aqui você pode adicionar métodos de consulta personalizados, se precisar,
    // mas para salvar, JpaRepository já oferece tudo que precisamos!
}