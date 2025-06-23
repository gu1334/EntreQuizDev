package br.com.entrequizdev.points.repository; // Ajuste o pacote conforme a estrutura do seu projeto

import br.com.entrequizdev.points.entity.UserAreaPoints;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UserAreaPointsRepository extends JpaRepository<UserAreaPoints, Long> {

    Optional<UserAreaPoints> findByUserIdAndAreaName(Long userId, String areaName);
//
//    List<UserAreaPoints> findByAreaNameOrderByCorrectAnswersDesc(String areaName);
//
//    List<UserAreaPoints> findTop10ByAreaNameOrderByCorrectAnswersDesc(String areaName);


}