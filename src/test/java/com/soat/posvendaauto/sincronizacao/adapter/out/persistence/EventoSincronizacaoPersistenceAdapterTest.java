package com.soat.posvendaauto.sincronizacao.adapter.out.persistence;

import com.soat.posvendaauto.sincronizacao.domain.EventoSincronizacao;
import com.soat.posvendaauto.sincronizacao.domain.StatusEvento;
import com.soat.posvendaauto.sincronizacao.domain.TipoEvento;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventoSincronizacaoPersistenceAdapterTest {

    @Mock
    private SpringDataEventoSincronizacaoRepository jpaRepository;

    @Test
    void deveSalvarEMapearDeVoltaParaDomain() {
        EventoSincronizacaoPersistenceAdapter adapter = new EventoSincronizacaoPersistenceAdapter(jpaRepository);
        EventoSincronizacao evento = EventoSincronizacao.criar(UUID.randomUUID(), TipoEvento.VEICULO_CRIADO, "{}");
        when(jpaRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        EventoSincronizacao salvo = adapter.save(evento);

        assertThat(salvo.getVeiculoId()).isEqualTo(evento.getVeiculoId());
    }

    @Test
    void deveBuscarPendentesEMapearParaDomain() {
        EventoSincronizacaoPersistenceAdapter adapter = new EventoSincronizacaoPersistenceAdapter(jpaRepository);
        EventoSincronizacaoJpaEntity entity = new EventoSincronizacaoJpaEntity(UUID.randomUUID(), UUID.randomUUID(),
                TipoEvento.VEICULO_CRIADO, "{}", StatusEvento.PENDENTE, 0, Instant.now(), Instant.now(), Instant.now());
        when(jpaRepository.findByStatusAndProximaTentativaEmLessThanEqual(eq(StatusEvento.PENDENTE), any()))
                .thenReturn(List.of(entity));

        List<EventoSincronizacao> pendentes = adapter.findByStatusAndProximaTentativaEmLessThanEqual(StatusEvento.PENDENTE, Instant.now());

        assertThat(pendentes).hasSize(1);
        assertThat(pendentes.get(0).getId()).isEqualTo(entity.getId());
    }
}
