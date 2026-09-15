package com.soat.posvendaauto.veiculo.application;

import java.util.UUID;

public class VeiculoNaoEncontradoException extends RuntimeException {

    private final UUID veiculoId;

    public VeiculoNaoEncontradoException(UUID veiculoId) {
        super("Veículo não encontrado: " + veiculoId);
        this.veiculoId = veiculoId;
    }

    public UUID getVeiculoId() {
        return veiculoId;
    }
}
