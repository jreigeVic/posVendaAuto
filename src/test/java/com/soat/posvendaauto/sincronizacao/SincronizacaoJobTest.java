package com.soat.posvendaauto.sincronizacao;

import com.soat.posvendaauto.auditoria.AuditoriaService;
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
class SincronizacaoJobTest {

    @Mock
    private EventoSincronizacaoRepository repository;

    @Mock
    private VendaVeiculosClient client;

    @Mock
    private AuditoriaService auditoriaService;

    private final JsonMapper objectMapper = JsonMapper.builder().build();

    private EventoSincronizacao eventoPendente() {
        String payload = objectMapper.writeValueAsString(new VeiculoSyncPayload(
                UUID.randomUUID(), "Fiat", "Argo", 2022, "Prata", java.math.BigDecimal.valueOf(78900), com.soat.posvendaauto.veiculo.EstadoConservacao.SEMINOVO));
        return EventoSincronizacao.criar(UUID.randomUUID(), TipoEvento.VEICULO_CRIADO, payload);
    }

    @Test
    void deveMarcarEventoComoEntregueQuandoSincronizacaoTemSucesso() {
        SincronizacaoJob job = new SincronizacaoJob(repository, client, auditoriaService, objectMapper);
        EventoSincronizacao evento = eventoPendente();

        when(repository.findByStatusAndProximaTentativaEmLessThanEqual(eq(StatusEvento.PENDENTE), any())).thenReturn(List.of(evento));
        when(client.sincronizar(any(), any(), any())).thenReturn(true);

        job.reenviarPendentes();

        assertThat(evento.getStatus()).isEqualTo(StatusEvento.ENTREGUE);
    }

    @Test
    void deveRegistrarFalhaComBackoffQuandoSincronizacaoFalha() {
        SincronizacaoJob job = new SincronizacaoJob(repository, client, auditoriaService, objectMapper);
        EventoSincronizacao evento = eventoPendente();

        when(repository.findByStatusAndProximaTentativaEmLessThanEqual(eq(StatusEvento.PENDENTE), any())).thenReturn(List.of(evento));
        when(client.sincronizar(any(), any(), any())).thenReturn(false);

        job.reenviarPendentes();

        assertThat(evento.getStatus()).isEqualTo(StatusEvento.PENDENTE);
        assertThat(evento.getTentativas()).isEqualTo(1);
        assertThat(evento.getProximaTentativaEm()).isAfter(Instant.now());
    }

    @Test
    void deveMarcarFalhaDefinitivaAposMaximoDeTentativas() {
        SincronizacaoJob job = new SincronizacaoJob(repository, client, auditoriaService, objectMapper);
        EventoSincronizacao evento = eventoPendente();
        for (int i = 0; i < 9; i++) {
            evento.registrarFalha(java.time.Duration.ZERO);
        }

        when(repository.findByStatusAndProximaTentativaEmLessThanEqual(eq(StatusEvento.PENDENTE), any())).thenReturn(List.of(evento));
        when(client.sincronizar(any(), any(), any())).thenReturn(false);

        job.reenviarPendentes();

        assertThat(evento.getStatus()).isEqualTo(StatusEvento.FALHOU_DEFINITIVAMENTE);
    }
}
