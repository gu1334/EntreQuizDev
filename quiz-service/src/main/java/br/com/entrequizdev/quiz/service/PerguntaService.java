package br.com.entrequizdev.quiz.service;


import br.com.entrequizdev.quiz.dto.AtualizarPerguntaDTO;
import br.com.entrequizdev.quiz.dto.PerguntaDTO;
import br.com.entrequizdev.quiz.entity.Pergunta;
import br.com.entrequizdev.quiz.enums.AreasEnum;
import br.com.entrequizdev.quiz.exception.DadosInvalidosException;
import br.com.entrequizdev.quiz.repository.PerguntaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import static org.apache.logging.log4j.util.Strings.isNotBlank;

@Service
public class PerguntaService {

    @Autowired
    private PerguntaRepository perguntaRepository;


    public Pergunta createOrUptadeQuestion(PerguntaDTO perguntaDTO) {

// valida se o projeto esta com a pergunta, resposta certa, resposta errada 1 2 e 3 preenchidas, caso contrario retorna erro
        if (!isNotBlank(perguntaDTO.getPergunta()) ||
                !isNotBlank(perguntaDTO.getResposta_certa()) || // <-- adicionar
                !isNotBlank(perguntaDTO.getResposta_errada_1()) ||
                !isNotBlank(perguntaDTO.getResposta_errada_2()) ||
                !isNotBlank(perguntaDTO.getResposta_errada_3())) {
            throw new DadosInvalidosException("Campos obrigatórios não preenchidos.");
        }
        if (perguntaDTO.getAreas() == null || perguntaDTO.getAreas().equals("")) {
            throw new DadosInvalidosException("Area obrigatória não preenchida.");
        }
        Set<AreasEnum> areasEnums = EnumSet.of(
                AreasEnum.BACKEND_JR,
                AreasEnum.BACKEND_PLENO,
                AreasEnum.BACKEND_SENIOR,
                AreasEnum.FRONTEND_JR,
                AreasEnum.FRONTEND_PLENO,
                AreasEnum.FRONTEND_SENIOR,
                AreasEnum.DEVOPS_JR,
                AreasEnum.DEVOPS_PLENO,
                AreasEnum.DEVOPS_SENIOR,
                AreasEnum.ESTAGIO,
                AreasEnum.SEM_AREA
        );

        if (!areasEnums.contains(perguntaDTO.getAreas())) {
            throw new DadosInvalidosException("Area errada");
        }

        // valida se o projeto se o id existe e se sim atualiza o mesmo

        if (perguntaDTO.getId() != null) {

            Pergunta projetoExistente = (perguntaDTO.getId() != null)
                    ? perguntaRepository.findById(perguntaDTO.getId()).orElseThrow(() ->
                    new EntityNotFoundException("Pergunta com ID " + perguntaDTO.getId() + " não encontrada."))
                    : new Pergunta();

            projetoExistente.setPergunta(perguntaDTO.getPergunta());
            projetoExistente.setResposta_certa(perguntaDTO.getResposta_certa());
            projetoExistente.setResposta_errada_1(perguntaDTO.getResposta_errada_1());
            projetoExistente.setResposta_errada_2(perguntaDTO.getResposta_errada_2());
            projetoExistente.setResposta_errada_3(perguntaDTO.getResposta_errada_3());
            projetoExistente.setResposta_errada_4(perguntaDTO.getResposta_errada_4());
            projetoExistente.setResposta_errada_5(perguntaDTO.getResposta_errada_5());
            projetoExistente.setResposta_errada_6(perguntaDTO.getResposta_errada_6());
            projetoExistente.setResposta_errada_7(perguntaDTO.getResposta_errada_7());
            projetoExistente.setArea(String.valueOf(perguntaDTO.getAreas()));

            return perguntaRepository.save(projetoExistente);
        }

        Pergunta novaPergunta = new Pergunta();
        novaPergunta.setPergunta(perguntaDTO.getPergunta());
        novaPergunta.setResposta_certa(perguntaDTO.getResposta_certa());
        novaPergunta.setResposta_errada_1(perguntaDTO.getResposta_errada_1());
        novaPergunta.setResposta_errada_2(perguntaDTO.getResposta_errada_2());
        novaPergunta.setResposta_errada_3(perguntaDTO.getResposta_errada_3());
        novaPergunta.setResposta_errada_4(perguntaDTO.getResposta_errada_4());
        novaPergunta.setResposta_errada_5(perguntaDTO.getResposta_errada_5());
        novaPergunta.setResposta_errada_6(perguntaDTO.getResposta_errada_6());
        novaPergunta.setResposta_errada_7(perguntaDTO.getResposta_errada_7());
        novaPergunta.setArea(String.valueOf(perguntaDTO.getAreas()));


        return perguntaRepository.save(novaPergunta);
    }


    // Atualiza o projeto (sem atualizar as variações dele) usando path
    public Object atualizarPergunta(Long id, AtualizarPerguntaDTO atualizarPerguntaDTO) {


        Pergunta projetoExistente = perguntaRepository.findById(id).orElseThrow(()
                -> new DadosInvalidosException("Projeto não encontrado"));

        if (atualizarPerguntaDTO.pergunta() != null) {
            projetoExistente.setPergunta(atualizarPerguntaDTO.pergunta());
        }
        if (atualizarPerguntaDTO.resposta_certa() != null) {
            projetoExistente.setResposta_certa(atualizarPerguntaDTO.resposta_certa());
        }
        if (atualizarPerguntaDTO.resposta_errada_1() != null) {
            projetoExistente.setResposta_errada_1(atualizarPerguntaDTO.resposta_errada_1());
        }
        if (atualizarPerguntaDTO.resposta_errada_2() != null) {
            projetoExistente.setResposta_errada_2(atualizarPerguntaDTO.resposta_errada_2());
        }
        if (atualizarPerguntaDTO.resposta_errada_3() != null) {
            projetoExistente.setResposta_errada_3(atualizarPerguntaDTO.resposta_errada_3());
        }
        if (atualizarPerguntaDTO.resposta_errada_4() != null) {
            projetoExistente.setResposta_errada_4(atualizarPerguntaDTO.resposta_errada_4());
        }
        if (atualizarPerguntaDTO.resposta_errada_5() != null) {
            projetoExistente.setResposta_errada_5(atualizarPerguntaDTO.resposta_errada_5());
        }
        if (atualizarPerguntaDTO.resposta_errada_6() != null) {
            projetoExistente.setResposta_errada_6(atualizarPerguntaDTO.resposta_errada_6());
        }
        if (atualizarPerguntaDTO.resposta_errada_7() != null) {
            projetoExistente.setResposta_errada_7(atualizarPerguntaDTO.resposta_errada_7());
        }
        // Atualiza área, se fornecida
        if (atualizarPerguntaDTO.areas() != null) {

            try {
                AreasEnum areaEnum = AreasEnum.valueOf(String.valueOf(atualizarPerguntaDTO.areas()));
                projetoExistente.setArea(areaEnum.name());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body("Área inválida: " + atualizarPerguntaDTO.areas());
            }
        }
        return perguntaRepository.save(projetoExistente);

    }

    public List<Pergunta> buscarTodasPerguntas() {
        return perguntaRepository.findAll();
    }

    public void deletarPergunta(Long id) {
        if (!perguntaRepository.existsById(id)) {
            throw new EntityNotFoundException("Pergunta com id " + id + " não encontrada.");
        }
        perguntaRepository.deleteById(id);
    }

}
