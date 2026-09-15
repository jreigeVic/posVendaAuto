package com.soat.posvendaauto.sincronizacao.application;

import com.soat.posvendaauto.sincronizacao.application.port.out.EventoSincronizacaoRepositoryPort;
import com.soat.posvendaauto.sincronizacao.domain.EventoSincronizacao;
import com.soat.posvendaauto.sincronizacao.domain.StatusEvento;
import com.soat.posvendaauto.sincronizacao.domain.TipoEvento;
import com.soat.posvendaauto.veiculo.domain.EstadoConservacao;
import com.soat.posvendaauto.veiculo.domain.Veiculo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tools.jackson.databind.json.JsonMapper;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SincronizacaoServiceTest {

    @Mock
    private EventoSincronizacaoRepositoryPort repository;

    @Test
    void deveRegistrarEventoPendenteComPayloadSerializado() {
        SincronizacaoService service = new SincronizacaoService(repository, JsonMapper.builder().build());
        UUID id = UUID.randomUUID();
        Veiculo veiculo = new Veiculo(id, "Fiat", "Argo", 2022, "Prata", BigDecimal.valueOf(78900),
                EstadoConservacao.SEMINOVO, Instant.now(), Instant.now());

        service.registrarEvento(veiculo, TipoEvento.VEICULO_CRIADO);

        ArgumentCaptor<EventoSincronizacao> captor = ArgumentCaptor.forClass(EventoSincronizacao.class);
        verify(repository).save(captor.capture());
        EventoSincronizacao evento = captor.getValue();
        assertThat(evento.getStatus()).isEqualTo(StatusEvento.PENDENTE);
        assertThat(evento.getVeiculoId()).isEqualTo(id);
        assertThat(evento.getPayload()).contains("Fiat");
    }
}
