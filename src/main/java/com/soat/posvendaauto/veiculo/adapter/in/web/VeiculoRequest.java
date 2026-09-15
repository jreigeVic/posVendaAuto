package com.soat.posvendaauto.veiculo.adapter.in.web;

import com.soat.posvendaauto.veiculo.domain.EstadoConservacao;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record VeiculoRequest(
        @NotNull String marca,
        @NotNull String modelo,
        @NotNull Integer ano,
        @NotNull String cor,
        @NotNull BigDecimal preco,
        @NotNull EstadoConservacao estadoConservacao
) {
}
