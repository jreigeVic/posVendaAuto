package com.soat.posvendaauto.veiculo.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Veiculo {

    private UUID id;

    private String marca;

    private String modelo;

    private Integer ano;

    private String cor;

    private BigDecimal preco;

    private EstadoConservacao estadoConservacao;

    private Instant criadoEm;

    private Instant atualizadoEm;
}
