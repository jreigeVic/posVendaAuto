package com.soat.posvendaauto.veiculo.adapter.out.persistence;

import com.soat.posvendaauto.veiculo.domain.Veiculo;

public final class VeiculoMapper {

    private VeiculoMapper() {
    }

    public static Veiculo toDomain(VeiculoJpaEntity entity) {
        return new Veiculo(
                entity.getId(),
                entity.getMarca(),
                entity.getModelo(),
                entity.getAno(),
                entity.getCor(),
                entity.getPreco(),
                entity.getEstadoConservacao(),
                entity.getCriadoEm(),
                entity.getAtualizadoEm()
        );
    }

    public static VeiculoJpaEntity toJpaEntity(Veiculo veiculo) {
        return new VeiculoJpaEntity(
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
