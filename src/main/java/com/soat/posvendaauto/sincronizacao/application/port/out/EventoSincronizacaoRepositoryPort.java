package com.soat.posvendaauto.sincronizacao.application.port.out;

import com.soat.posvendaauto.sincronizacao.domain.EventoSincronizacao;
import com.soat.posvendaauto.sincronizacao.domain.StatusEvento;

import java.time.Instant;
import java.util.List;

public interface EventoSincronizacaoRepositoryPort {

    EventoSincronizacao save(EventoSincronizacao evento);

    List<EventoSincronizacao> findByStatusAndProximaTentativaEmLessThanEqual(StatusEvento status, Instant agora);
}
