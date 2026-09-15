package com.soat.posvendaauto.veiculo.application.port.in;

import com.soat.posvendaauto.veiculo.domain.Veiculo;

public interface CadastrarVeiculoUseCase {

    Veiculo cadastrar(DadosVeiculo dados);
}
