package com.soat.posvendaauto.sincronizacao.adapter.out.persistence;

import com.soat.posvendaauto.sincronizacao.domain.EventoSincronizacao;
import com.soat.posvendaauto.sincronizacao.domain.StatusEvento;
import com.soat.posvendaauto.sincronizacao.domain.TipoEvento;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class EventoSincronizacaoMapperTest {

    @Test
    void deveConverterDomainParaJpaEntityEDeVoltaSemPerderDados() {
        EventoSincronizacao evento = new EventoSincronizacao(UUID.randomUUID(), UUID.randomUUID(), TipoEvento.VEICULO_CRIADO,
                "{}", StatusEvento.PENDENTE, 0, Instant.now(), Instant.now(), Instant.now());

        EventoSincronizacaoJpaEntity entity = EventoSincronizacaoMapper.toJpaEntity(evento);
        EventoSincronizacao reconvertido = EventoSincronizacaoMapper.toDomain(entity);

        assertThat(entity.getId()).isEqualTo(evento.getId());
        assertThat(reconvertido.getVeiculoId()).isEqualTo(evento.getVeiculoId());
        assertThat(reconvertido.getStatus()).isEqualTo(evento.getStatus());
        assertThat(reconvertido.getPayload()).isEqualTo(evento.getPayload());
    }
}
