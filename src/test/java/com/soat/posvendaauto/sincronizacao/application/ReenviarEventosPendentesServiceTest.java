package com.soat.posvendaauto.sincronizacao.application;

import com.soat.posvendaauto.auditoria.application.port.out.AuditoriaPort;
import com.soat.posvendaauto.sincronizacao.application.port.out.EventoSincronizacaoRepositoryPort;
import com.soat.posvendaauto.sincronizacao.application.port.out.VendaVeiculosSyncPort;
import com.soat.posvendaauto.sincronizacao.application.port.out.VeiculoSyncPayload;
import com.soat.posvendaauto.sincronizacao.domain.EventoSincronizacao;
import com.soat.posvendaauto.sincronizacao.domain.StatusEvento;
import com.soat.posvendaauto.sincronizacao.domain.TipoEvento;
import com.soat.posvendaauto.veiculo.domain.EstadoConservacao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tools.jackson.databind.json.JsonMapper;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReenviarEventosPendentesServiceTest {

    @Mock
    private EventoSincronizacaoRepositoryPort repository;

    @Mock
    private VendaVeiculosSyncPort client;

    @Mock
    private AuditoriaPort auditoriaService;

    private final JsonMapper objectMapper = JsonMapper.builder().build();

    private EventoSincronizacao eventoPendente() {
        String payload = objectMapper.writeValueAsString(new VeiculoSyncPayload(
                UUID.randomUUID(), "Fiat", "Argo", 2022, "Prata", java.math.BigDecimal.valueOf(78900), EstadoConservacao.SEMINOVO));
        return EventoSincronizacao.criar(UUID.randomUUID(), TipoEvento.VEICULO_CRIADO, payload);
    }

    @Test
    void deveMarcarEventoComoEntregueQuandoSincronizacaoTemSucesso() {
        ReenviarEventosPendentesService service = new ReenviarEventosPendentesService(repository, client, auditoriaService, objectMapper);
        EventoSincronizacao evento = eventoPendente();

        when(repository.findByStatusAndProximaTentativaEmLessThanEqual(eq(StatusEvento.PENDENTE), any())).thenReturn(List.of(evento));
        when(client.sincronizar(any(), any(), any())).thenReturn(true);

        service.reenviarPendentes();

        assertThat(evento.getStatus()).isEqualTo(StatusEvento.ENTREGUE);
    }

    @Test
    void deveRegistrarFalhaComBackoffQuandoSincronizacaoFalha() {
        ReenviarEventosPendentesService service = new ReenviarEventosPendentesService(repository, client, auditoriaService, objectMapper);
        EventoSincronizacao evento = eventoPendente();

        when(repository.findByStatusAndProximaTentativaEmLessThanEqual(eq(StatusEvento.PENDENTE), any())).thenReturn(List.of(evento));
        when(client.sincronizar(any(), any(), any())).thenReturn(false);

        service.reenviarPendentes();

        assertThat(evento.getStatus()).isEqualTo(StatusEvento.PENDENTE);
        assertThat(evento.getTentativas()).isEqualTo(1);
        assertThat(evento.getProximaTentativaEm()).isAfter(Instant.now());
    }

    @Test
    void deveMarcarFalhaDefinitivaAposMaximoDeTentativas() {
        ReenviarEventosPendentesService service = new ReenviarEventosPendentesService(repository, client, auditoriaService, objectMapper);
        EventoSincronizacao evento = eventoPendente();
        for (int i = 0; i < 9; i++) {
            evento.registrarFalha(java.time.Duration.ZERO);
        }

        when(repository.findByStatusAndProximaTentativaEmLessThanEqual(eq(StatusEvento.PENDENTE), any())).thenReturn(List.of(evento));
        when(client.sincronizar(any(), any(), any())).thenReturn(false);

        service.reenviarPendentes();

        assertThat(evento.getStatus()).isEqualTo(StatusEvento.FALHOU_DEFINITIVAMENTE);
    }
}
