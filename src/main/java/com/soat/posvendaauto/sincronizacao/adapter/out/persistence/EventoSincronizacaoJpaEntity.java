package com.soat.posvendaauto.sincronizacao.adapter.out.persistence;

import com.soat.posvendaauto.sincronizacao.domain.StatusEvento;
import com.soat.posvendaauto.sincronizacao.domain.TipoEvento;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "evento_sincronizacao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventoSincronizacaoJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID veiculoId;

    @Enumerated(EnumType.STRING)
    private TipoEvento tipoEvento;

    @Lob
    private String payload;

    @Enumerated(EnumType.STRING)
    private StatusEvento status;

    private int tentativas;

    private Instant proximaTentativaEm;

    private Instant criadoEm;

    private Instant atualizadoEm;
}
