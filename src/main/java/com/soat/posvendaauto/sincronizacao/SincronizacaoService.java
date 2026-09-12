package com.soat.posvendaauto.sincronizacao;

import com.soat.posvendaauto.veiculo.Veiculo;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
public class SincronizacaoService {

    private final EventoSincronizacaoRepository repository;
    private final ObjectMapper objectMapper;

    public SincronizacaoService(EventoSincronizacaoRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    public void registrarEvento(Veiculo veiculo, TipoEvento tipoEvento) {
        String payloadJson = objectMapper.writeValueAsString(VeiculoSyncPayload.de(veiculo));
        repository.save(EventoSincronizacao.criar(veiculo.getId(), tipoEvento, payloadJson));
    }
}
