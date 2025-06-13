package br.com.entrequizdev.quiz.service;

import br.com.entrequizdev.quiz.dto.AleatorioRespostasDTO;
import br.com.entrequizdev.quiz.dto.AtualizarPerguntaDTO;
import br.com.entrequizdev.quiz.dto.PerguntaDTO;
import br.com.entrequizdev.quiz.entity.Pergunta;
import br.com.entrequizdev.quiz.entity.RespostaIncorreta; // Importe a nova entidade
import br.com.entrequizdev.quiz.enums.AreasEnum;
import br.com.entrequizdev.quiz.exception.DadosInvalidosException;
import br.com.entrequizdev.quiz.repository.PerguntaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Import para @Transactional

import java.util.*;
import java.util.stream.Collectors;

import static org.apache.logging.log4j.util.Strings.isNotBlank; // Ok para manter

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
                        ri.setPergunta(entity); // Garante a referência bidirecional
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

        // Mapear respostas incorretas da entidade para DTO
        if (entity.getRespostasIncorretas() != null) {
            dto.setRespostasIncorretas(entity.getRespostasIncorretas().stream()
                    .map(RespostaIncorreta::getTextoResposta)
                    .collect(Collectors.toList()));
        }
        dto.setArea(entity.getArea());
        return dto;
    }


    // --- Métodos de Serviço ---

    @Transactional // Garante que a operação seja atômica
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
            // Cria nova pergunta
            perguntaEntity = toEntity(perguntaDTO); // Usa o método de mapeamento para criar a entidade
        }

        Pergunta savedPergunta = perguntaRepository.save(perguntaEntity);
        return toDTO(savedPergunta); // Retorna o DTO da pergunta salva
    }

    @Transactional
    public PerguntaDTO atualizarPergunta(Long id, AtualizarPerguntaDTO atualizarPerguntaDTO) { // Retornar DTO

        Pergunta perguntaExistente = perguntaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pergunta com ID " + id + " não encontrada."));

        // Aplica atualizações somente se o campo não for nulo no DTO
        if (isNotBlank(atualizarPerguntaDTO.pergunta())) {
            perguntaExistente.setPergunta(atualizarPerguntaDTO.pergunta());
        }
        if (isNotBlank(atualizarPerguntaDTO.respostaCorreta())) { // Usando o novo nome do campo
            perguntaExistente.setRespostaCorreta(atualizarPerguntaDTO.respostaCorreta());
        }

        // Atualização de respostas incorretas:
        // Se o DTO de atualização contém uma lista de respostas incorretas,
        // assumimos que é uma substituição completa das respostas existentes.
        if (atualizarPerguntaDTO.respostasIncorretas() != null) {
            perguntaExistente.getRespostasIncorretas().clear(); // Limpa as antigas
            atualizarPerguntaDTO.respostasIncorretas().forEach(textoResposta -> {
                RespostaIncorreta novaResposta = new RespostaIncorreta();
                novaResposta.setTextoResposta(textoResposta);
                perguntaExistente.addRespostaIncorreta(novaResposta);
            });
        }

        // Atualiza área, se fornecida
        if (atualizarPerguntaDTO.area() != null) { // Agora o campo é 'area' e já é um Enum
            perguntaExistente.setArea(atualizarPerguntaDTO.area());
        }

        Pergunta updatedPergunta = perguntaRepository.save(perguntaExistente);
        return toDTO(updatedPergunta); // Retorna o DTO da pergunta atualizada
    }

    public List<PerguntaDTO> buscarTodasPerguntas() { // Retornar lista de DTOs
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