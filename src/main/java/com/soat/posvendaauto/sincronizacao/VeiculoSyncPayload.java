package com.soat.posvendaauto.sincronizacao;

import com.soat.posvendaauto.veiculo.EstadoConservacao;
import com.soat.posvendaauto.veiculo.Veiculo;

import java.math.BigDecimal;
import java.util.UUID;

public record VeiculoSyncPayload(
        UUID id,
        String marca,
        String modelo,
        Integer ano,
        String cor,
        BigDecimal preco,
        EstadoConservacao estadoConservacao
) {

    public static VeiculoSyncPayload de(Veiculo veiculo) {
        return new VeiculoSyncPayload(
                veiculo.getId(),
                veiculo.getMarca(),
                veiculo.getModelo(),
                veiculo.getAno(),
                veiculo.getCor(),
                veiculo.getPreco(),
                veiculo.getEstadoConservacao()
        );
    }
}
