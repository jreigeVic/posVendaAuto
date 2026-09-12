package com.soat.posvendaauto.veiculo;

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
