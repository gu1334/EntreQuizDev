package br.com.EntreQuizDev.service;

import br.com.EntreQuizDev.dto.PerguntaDTO;
import br.com.EntreQuizDev.entity.Pergunta;
import br.com.EntreQuizDev.exception.DadosInvalidosException;
import br.com.EntreQuizDev.repository.PerguntaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.apache.logging.log4j.util.Strings.isNotBlank;

@Service
public class PerguntaService {

    @Autowired
    private PerguntaRepository perguntaRepository;

    public Pergunta createOrUptadeQuestion(PerguntaDTO perguntaDTO) {

// valida se o projeto esta com a pergunta, resposta certa, resposta errada 1 2 e 3 preenchidas, caso contrario retorna erro
        if (!isNotBlank(perguntaDTO.getPergunta()) ||
                !isNotBlank(perguntaDTO.getResposta_errada_1()) ||
                !isNotBlank(perguntaDTO.getResposta_errada_2()) ||
                !isNotBlank(perguntaDTO.getResposta_errada_3())) {
            throw new DadosInvalidosException("Campos obrigatórios não preenchidos.");
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

            return perguntaRepository.save(novaPergunta);
    }


    // Atualiza o projeto (sem atualizar as variações dele) usando path
    public Pergunta atualizarPergunta(Long id, AtualizarPergunta atualizarPergunta) {


        Pergunta projetoExistente = perguntaRepository.findById(id).orElseThrow(()
                -> new DadosInvalidosException("Projeto não encontrado"));

        if (atualizarPergunta.pergunta() != null) {
            projetoExistente.setPergunta(atualizarPergunta.pergunta());
        }
        if (atualizarPergunta.resposta_certa() != null) {
            projetoExistente.setResposta_certa(atualizarPergunta.resposta_certa());
        }
        if (atualizarPergunta.resposta_errada_1() != null) {
            projetoExistente.setResposta_errada_1(atualizarPergunta.resposta_errada_1());
        }
        if (atualizarPergunta.resposta_errada_2() != null) {
            projetoExistente.setResposta_errada_2(atualizarPergunta.resposta_errada_2() );
        }
        if (atualizarPergunta.resposta_errada_3() != null) {
            projetoExistente.setResposta_errada_3(atualizarPergunta.resposta_errada_3());
        }
        if (atualizarPergunta.resposta_errada_4() != null) {
            projetoExistente.setResposta_errada_4(atualizarPergunta.resposta_errada_4());
        }
        if (atualizarPergunta.resposta_errada_5() != null) {
            projetoExistente.setResposta_errada_5(atualizarPergunta.resposta_errada_5());
        }
        if (atualizarPergunta.resposta_errada_6() != null) {
            projetoExistente.setResposta_errada_6(atualizarPergunta.resposta_errada_6());
        }
        if (atualizarPergunta.resposta_errada_7() != null) {
            projetoExistente.setResposta_errada_7(atualizarPergunta.resposta_errada_7());
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
