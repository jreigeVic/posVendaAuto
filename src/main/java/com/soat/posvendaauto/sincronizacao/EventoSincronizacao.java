package com.soat.posvendaauto.sincronizacao;

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
public class EventoSincronizacao {

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

    public static EventoSincronizacao criar(UUID veiculoId, TipoEvento tipoEvento, String payloadJson) {
        Instant agora = Instant.now();
        return new EventoSincronizacao(null, veiculoId, tipoEvento, payloadJson, StatusEvento.PENDENTE, 0, agora, agora, agora);
    }

    public void marcarEntregue() {
        this.status = StatusEvento.ENTREGUE;
        this.atualizadoEm = Instant.now();
    }

    public void registrarFalha(java.time.Duration backoff) {
        this.tentativas++;
        this.proximaTentativaEm = Instant.now().plus(backoff);
        this.atualizadoEm = Instant.now();
    }
}
