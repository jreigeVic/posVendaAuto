package com.soat.posvendaauto.veiculo.application.port.out;

import com.soat.posvendaauto.veiculo.domain.Veiculo;

import java.util.Optional;
import java.util.UUID;

public interface VeiculoRepositoryPort {

    Veiculo save(Veiculo veiculo);

    Optional<Veiculo> findById(UUID id);
}
