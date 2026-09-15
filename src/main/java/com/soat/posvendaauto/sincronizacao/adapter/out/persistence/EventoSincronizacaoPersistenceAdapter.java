package com.soat.posvendaauto.sincronizacao.adapter.out.persistence;

import com.soat.posvendaauto.sincronizacao.application.port.out.EventoSincronizacaoRepositoryPort;
import com.soat.posvendaauto.sincronizacao.domain.EventoSincronizacao;
import com.soat.posvendaauto.sincronizacao.domain.StatusEvento;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class EventoSincronizacaoPersistenceAdapter implements EventoSincronizacaoRepositoryPort {

    private final SpringDataEventoSincronizacaoRepository jpaRepository;

    public EventoSincronizacaoPersistenceAdapter(SpringDataEventoSincronizacaoRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public EventoSincronizacao save(EventoSincronizacao evento) {
        EventoSincronizacaoJpaEntity salvo = jpaRepository.save(EventoSincronizacaoMapper.toJpaEntity(evento));
        return EventoSincronizacaoMapper.toDomain(salvo);
    }

    @Override
    public List<EventoSincronizacao> findByStatusAndProximaTentativaEmLessThanEqual(StatusEvento status, Instant agora) {
        return jpaRepository.findByStatusAndProximaTentativaEmLessThanEqual(status, agora).stream()
                .map(EventoSincronizacaoMapper::toDomain)
                .toList();
    }
}
