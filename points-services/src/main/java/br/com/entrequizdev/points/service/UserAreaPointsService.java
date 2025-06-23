package br.com.entrequizdev.points.service;

import br.com.entrequizdev.points.repository.UserAreaPointsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserAreaPointsService {

    @Autowired
    private UserAreaPointsRepository userAreaPointsRepository;

    public void criarAtualizarPontuação (){

    }

}
