package com.soat.posvendaauto.sincronizacao.application;

import com.soat.posvendaauto.sincronizacao.application.port.out.EventoSincronizacaoRepositoryPort;
import com.soat.posvendaauto.sincronizacao.application.port.out.SincronizacaoEventoPort;
import com.soat.posvendaauto.sincronizacao.application.port.out.VeiculoSyncPayload;
import com.soat.posvendaauto.sincronizacao.domain.EventoSincronizacao;
import com.soat.posvendaauto.sincronizacao.domain.TipoEvento;
import com.soat.posvendaauto.veiculo.domain.Veiculo;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
public class SincronizacaoService implements SincronizacaoEventoPort {

    private final EventoSincronizacaoRepositoryPort repository;
    private final ObjectMapper objectMapper;

    public SincronizacaoService(EventoSincronizacaoRepositoryPort repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void registrarEvento(Veiculo veiculo, TipoEvento tipoEvento) {
        String payloadJson = objectMapper.writeValueAsString(VeiculoSyncPayload.de(veiculo));
        repository.save(EventoSincronizacao.criar(veiculo.getId(), tipoEvento, payloadJson));
    }
}
