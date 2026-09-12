package com.soat.posvendaauto.sincronizacao;

import com.soat.posvendaauto.auditoria.AuditoriaService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Component
public class SincronizacaoJob {

    private static final int MAX_TENTATIVAS = 10;
    private static final Duration BACKOFF_BASE = Duration.ofSeconds(5);

    private final EventoSincronizacaoRepository repository;
    private final VendaVeiculosClient client;
    private final AuditoriaService auditoriaService;
    private final ObjectMapper objectMapper;

    public SincronizacaoJob(EventoSincronizacaoRepository repository, VendaVeiculosClient client,
                             AuditoriaService auditoriaService, ObjectMapper objectMapper) {
        this.repository = repository;
        this.client = client;
        this.auditoriaService = auditoriaService;
        this.objectMapper = objectMapper;
    }

    @Scheduled(fixedDelayString = "${sincronizacao.job.intervalo-ms:5000}")
    @Transactional
    public void reenviarPendentes() {
        List<EventoSincronizacao> pendentes = repository.findByStatusAndProximaTentativaEmLessThanEqual(StatusEvento.PENDENTE, Instant.now());
        for (EventoSincronizacao evento : pendentes) {
            processar(evento);
        }
    }

    private void processar(EventoSincronizacao evento) {
        VeiculoSyncPayload payload = objectMapper.readValue(evento.getPayload(), VeiculoSyncPayload.class);
        boolean sucesso = client.sincronizar(evento.getTipoEvento(), evento.getVeiculoId(), payload);

        if (sucesso) {
            evento.marcarEntregue();
            repository.save(evento);
            auditoriaService.registrarSucesso("SINCRONIZAR_VEICULO", evento.getVeiculoId(),
                    "Evento " + evento.getTipoEvento() + " entregue ao serviço de venda");
            return;
        }

        Duration backoff = BACKOFF_BASE.multipliedBy((long) Math.pow(2, Math.min(evento.getTentativas(), 6)));
        evento.registrarFalha(backoff);

        if (evento.getTentativas() >= MAX_TENTATIVAS) {
            evento.setStatus(StatusEvento.FALHOU_DEFINITIVAMENTE);
            auditoriaService.registrarErro("SINCRONIZAR_VEICULO", evento.getVeiculoId(),
                    "Falhou definitivamente após " + evento.getTentativas() + " tentativas");
        }
        repository.save(evento);
    }
}
