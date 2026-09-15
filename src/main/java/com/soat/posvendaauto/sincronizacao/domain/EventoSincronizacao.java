package com.soat.posvendaauto.sincronizacao.domain;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

public class EventoSincronizacao {

    private UUID id;
    private UUID veiculoId;
    private TipoEvento tipoEvento;
    private String payload;
    private StatusEvento status;
    private int tentativas;
    private Instant proximaTentativaEm;
    private Instant criadoEm;
    private Instant atualizadoEm;

    public EventoSincronizacao(UUID id, UUID veiculoId, TipoEvento tipoEvento, String payload, StatusEvento status,
                                int tentativas, Instant proximaTentativaEm, Instant criadoEm, Instant atualizadoEm) {
        this.id = id;
        this.veiculoId = veiculoId;
        this.tipoEvento = tipoEvento;
        this.payload = payload;
        this.status = status;
        this.tentativas = tentativas;
        this.proximaTentativaEm = proximaTentativaEm;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
    }

    public static EventoSincronizacao criar(UUID veiculoId, TipoEvento tipoEvento, String payloadJson) {
        Instant agora = Instant.now();
        return new EventoSincronizacao(null, veiculoId, tipoEvento, payloadJson, StatusEvento.PENDENTE, 0, agora, agora, agora);
    }

    public void marcarEntregue() {
        this.status = StatusEvento.ENTREGUE;
        this.atualizadoEm = Instant.now();
    }

    public void registrarFalha(Duration backoff) {
        this.tentativas++;
        this.proximaTentativaEm = Instant.now().plus(backoff);
        this.atualizadoEm = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public UUID getVeiculoId() {
        return veiculoId;
    }

    public TipoEvento getTipoEvento() {
        return tipoEvento;
    }

    public String getPayload() {
        return payload;
    }

    public StatusEvento getStatus() {
        return status;
    }

    public void setStatus(StatusEvento status) {
        this.status = status;
    }

    public int getTentativas() {
        return tentativas;
    }

    public Instant getProximaTentativaEm() {
        return proximaTentativaEm;
    }

    public Instant getCriadoEm() {
        return criadoEm;
    }

    public Instant getAtualizadoEm() {
        return atualizadoEm;
    }
}
