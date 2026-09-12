package com.soat.posvendaauto.sincronizacao;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface EventoSincronizacaoRepository extends JpaRepository<EventoSincronizacao, UUID> {

    List<EventoSincronizacao> findByStatusAndProximaTentativaEmLessThanEqual(StatusEvento status, Instant agora);
}
