package br.com.EntreQuizDev.service;

import br.com.EntreQuizDev.dto.PerguntaDTO;
import br.com.EntreQuizDev.entity.Pergunta;
import br.com.EntreQuizDev.exception.DadosInvalidosException;
import br.com.EntreQuizDev.repository.PerguntaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        PerguntaDTO perguntaDTO = new PerguntaDTO();

        Pergunta perguntaAntiga = perguntaRepository.findById(id).orElse(null);

        if (perguntaDTO.getPergunta() != null) {
            perguntaAntiga.setPergunta(perguntaDTO.getPergunta());
        }
        if (perguntaDTO.getResposta_certa() != null) {
            perguntaAntiga.setResposta_certa(perguntaDTO.getResposta_certa());
        }
        if (perguntaDTO.getResposta_errada_1() != null) {
            perguntaAntiga.setResposta_errada_1(perguntaDTO.getResposta_errada_1());
        }
        if (perguntaDTO.getResposta_errada_2() != null) {
            perguntaAntiga.setResposta_errada_2(perguntaDTO.getResposta_errada_2());
        }
        if (perguntaDTO.getResposta_errada_3() != null) {
            perguntaAntiga.setResposta_errada_3(perguntaDTO.getResposta_errada_3());
        }
        if (perguntaDTO.getResposta_errada_4() != null) {
            perguntaAntiga.setResposta_errada_4(perguntaDTO.getResposta_errada_4());
        }
        if (perguntaDTO.getResposta_errada_5() != null) {
            perguntaAntiga.setResposta_errada_5(perguntaDTO.getResposta_errada_5());
        }
        if (perguntaDTO.getResposta_errada_6() != null) {
            perguntaAntiga.setResposta_errada_6(perguntaDTO.getResposta_errada_6());
        }
        if (perguntaDTO.getResposta_errada_7() != null) {
            perguntaAntiga.setResposta_errada_7(perguntaDTO.getResposta_errada_7());
        }


        return perguntaRepository.save(perguntaAntiga);

    }
}
