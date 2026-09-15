package com.soat.posvendaauto.veiculo.application.port.in;

import com.soat.posvendaauto.veiculo.domain.EstadoConservacao;

import java.math.BigDecimal;

public record DadosVeiculo(
        String marca,
        String modelo,
        Integer ano,
        String cor,
        BigDecimal preco,
        EstadoConservacao estadoConservacao
) {
}
