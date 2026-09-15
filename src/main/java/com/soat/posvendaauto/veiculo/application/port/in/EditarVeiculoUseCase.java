package com.soat.posvendaauto.veiculo.application.port.in;

import com.soat.posvendaauto.veiculo.domain.Veiculo;

import java.util.UUID;

public interface EditarVeiculoUseCase {

    Veiculo editar(UUID id, DadosVeiculo dados);
}
