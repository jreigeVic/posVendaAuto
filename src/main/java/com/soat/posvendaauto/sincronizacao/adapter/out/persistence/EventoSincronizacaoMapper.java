package com.soat.posvendaauto.sincronizacao.adapter.out.persistence;

import com.soat.posvendaauto.sincronizacao.domain.EventoSincronizacao;

public final class EventoSincronizacaoMapper {

    private EventoSincronizacaoMapper() {
    }

    public static EventoSincronizacao toDomain(EventoSincronizacaoJpaEntity entity) {
        return new EventoSincronizacao(
                entity.getId(),
                entity.getVeiculoId(),
                entity.getTipoEvento(),
                entity.getPayload(),
                entity.getStatus(),
                entity.getTentativas(),
                entity.getProximaTentativaEm(),
                entity.getCriadoEm(),
                entity.getAtualizadoEm()
        );
    }

    public static EventoSincronizacaoJpaEntity toJpaEntity(EventoSincronizacao evento) {
        return new EventoSincronizacaoJpaEntity(
                evento.getId(),
                evento.getVeiculoId(),
                evento.getTipoEvento(),
                evento.getPayload(),
                evento.getStatus(),
                evento.getTentativas(),
                evento.getProximaTentativaEm(),
                evento.getCriadoEm(),
                evento.getAtualizadoEm()
        );
    }
}
