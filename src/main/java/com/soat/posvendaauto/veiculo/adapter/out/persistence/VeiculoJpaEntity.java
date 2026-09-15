package com.soat.posvendaauto.veiculo.adapter.out.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import com.soat.posvendaauto.veiculo.domain.EstadoConservacao;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "veiculo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VeiculoJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String marca;

    private String modelo;

    private Integer ano;

    private String cor;

    private BigDecimal preco;

    @Enumerated(EnumType.STRING)
    private EstadoConservacao estadoConservacao;

    private Instant criadoEm;

    private Instant atualizadoEm;
}
