package com.soat.posvendaauto.veiculo;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record VeiculoResponse(
        UUID id,
        String marca,
        String modelo,
        Integer ano,
        String cor,
        BigDecimal preco,
        EstadoConservacao estadoConservacao,
        Instant criadoEm,
        Instant atualizadoEm
) {

    public static VeiculoResponse de(Veiculo veiculo) {
        return new VeiculoResponse(
                veiculo.getId(),
                veiculo.getMarca(),
                veiculo.getModelo(),
                veiculo.getAno(),
                veiculo.getCor(),
                veiculo.getPreco(),
                veiculo.getEstadoConservacao(),
                veiculo.getCriadoEm(),
                veiculo.getAtualizadoEm()
        );
    }
}
