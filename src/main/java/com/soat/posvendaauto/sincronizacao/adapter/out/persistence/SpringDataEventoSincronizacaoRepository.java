package com.soat.posvendaauto.sincronizacao.adapter.out.persistence;

import com.soat.posvendaauto.sincronizacao.domain.StatusEvento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface SpringDataEventoSincronizacaoRepository extends JpaRepository<EventoSincronizacaoJpaEntity, UUID> {

    List<EventoSincronizacaoJpaEntity> findByStatusAndProximaTentativaEmLessThanEqual(StatusEvento status, Instant agora);
}
