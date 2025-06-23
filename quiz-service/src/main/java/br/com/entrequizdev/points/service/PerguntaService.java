package br.com.entrequizdev.points.service;

import br.com.entrequizdev.points.dto.AtualizarPerguntaDTO;
import br.com.entrequizdev.points.dto.PerguntaDTO;
import br.com.entrequizdev.points.entity.Pergunta;
import br.com.entrequizdev.points.entity.RespostaIncorreta;
import br.com.entrequizdev.points.exception.DadosInvalidosException;
import br.com.entrequizdev.points.repository.PerguntaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.apache.logging.log4j.util.Strings.isNotBlank;

@Service
public class PerguntaService {

    @Autowired
    private PerguntaRepository perguntaRepository;

    private Pergunta toEntity(PerguntaDTO dto) {
        Pergunta entity = new Pergunta();
        entity.setId(dto.getId());
        entity.setPergunta(dto.getPergunta());
        entity.setRespostaCorreta(dto.getRespostaCorreta());

        if (dto.getRespostasIncorretas() != null) {
            List<RespostaIncorreta> respostasIncorretas = dto.getRespostasIncorretas().stream()
                    .map(texto -> {
                        RespostaIncorreta ri = new RespostaIncorreta();
                        ri.setTextoResposta(texto);
                        ri.setPergunta(entity);
                        return ri;
                    })
                    .collect(Collectors.toList());
            entity.setRespostasIncorretas(respostasIncorretas);
        }
        entity.setArea(dto.getArea());
        return entity;
    }

    private PerguntaDTO toDTO(Pergunta entity) {
        PerguntaDTO dto = new PerguntaDTO();
        dto.setId(entity.getId());
        dto.setPergunta(entity.getPergunta());
        dto.setRespostaCorreta(entity.getRespostaCorreta());

        if (entity.getRespostasIncorretas() != null) {
            dto.setRespostasIncorretas(entity.getRespostasIncorretas().stream()
                    .map(RespostaIncorreta::getTextoResposta)
                    .collect(Collectors.toList()));
        }
        dto.setArea(entity.getArea());
        return dto;
    }

    @Transactional
    public PerguntaDTO createOrUpdateQuestion(PerguntaDTO perguntaDTO) {

        if (!isNotBlank(perguntaDTO.getPergunta()) ||
                !isNotBlank(perguntaDTO.getRespostaCorreta()) ||
                perguntaDTO.getRespostasIncorretas() == null ||
                perguntaDTO.getRespostasIncorretas().isEmpty()) {
            throw new DadosInvalidosException("Campos 'pergunta', 'respostaCorreta' e ao menos uma 'respostaIncorreta' são obrigatórios.");
        }

        if (perguntaDTO.getArea() == null) {
            throw new DadosInvalidosException("Área obrigatória não preenchida.");
        }

        Pergunta perguntaEntity;

        if (perguntaDTO.getId() != null) {
            perguntaEntity = perguntaRepository.findById(perguntaDTO.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Pergunta com ID " + perguntaDTO.getId() + " não encontrada."));

            perguntaEntity.setPergunta(perguntaDTO.getPergunta());
            perguntaEntity.setRespostaCorreta(perguntaDTO.getRespostaCorreta());
            perguntaEntity.setArea(perguntaDTO.getArea());

            perguntaEntity.getRespostasIncorretas().clear();
            if (perguntaDTO.getRespostasIncorretas() != null) {
                perguntaDTO.getRespostasIncorretas().forEach(textoResposta -> {
                    RespostaIncorreta novaResposta = new RespostaIncorreta();
                    novaResposta.setTextoResposta(textoResposta);
                    perguntaEntity.addRespostaIncorreta(novaResposta);
                });
            }

        } else {
            perguntaEntity = toEntity(perguntaDTO);
        }

        Pergunta savedPergunta = perguntaRepository.save(perguntaEntity);
        return toDTO(savedPergunta);
    }

    @Transactional
    public PerguntaDTO atualizarPergunta(Long id, AtualizarPerguntaDTO atualizarPerguntaDTO) {

        Pergunta perguntaExistente = perguntaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pergunta com ID " + id + " não encontrada."));

        if (isNotBlank(atualizarPerguntaDTO.pergunta())) {
            perguntaExistente.setPergunta(atualizarPerguntaDTO.pergunta());
        }
        if (isNotBlank(atualizarPerguntaDTO.respostaCorreta())) {
            perguntaExistente.setRespostaCorreta(atualizarPerguntaDTO.respostaCorreta());
        }

        if (atualizarPerguntaDTO.respostasIncorretas() != null) {
            perguntaExistente.getRespostasIncorretas().clear();
            atualizarPerguntaDTO.respostasIncorretas().forEach(textoResposta -> {
                RespostaIncorreta novaResposta = new RespostaIncorreta();
                novaResposta.setTextoResposta(textoResposta);
                perguntaExistente.addRespostaIncorreta(novaResposta);
            });
        }

        if (atualizarPerguntaDTO.area() != null) {
            perguntaExistente.setArea(atualizarPerguntaDTO.area());
        }

        Pergunta updatedPergunta = perguntaRepository.save(perguntaExistente);
        return toDTO(updatedPergunta);
    }

    public List<PerguntaDTO> buscarTodasPerguntas() {
        return perguntaRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deletarPergunta(Long id) {
        if (!perguntaRepository.existsById(id)) {
            throw new EntityNotFoundException("Pergunta com ID " + id + " não encontrada.");
        }
        perguntaRepository.deleteById(id);
    }
}