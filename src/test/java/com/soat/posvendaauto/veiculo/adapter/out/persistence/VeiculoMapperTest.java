package com.soat.posvendaauto.veiculo.adapter.out.persistence;

import com.soat.posvendaauto.veiculo.domain.EstadoConservacao;
import com.soat.posvendaauto.veiculo.domain.Veiculo;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class VeiculoMapperTest {

    @Test
    void deveConverterDomainParaJpaEntityEDeVoltaSemPerderDados() {
        Veiculo veiculo = new Veiculo(UUID.randomUUID(), "Fiat", "Argo", 2022, "Prata",
                BigDecimal.valueOf(78900), EstadoConservacao.SEMINOVO, Instant.now(), Instant.now());

        VeiculoJpaEntity entity = VeiculoMapper.toJpaEntity(veiculo);
        Veiculo reconvertido = VeiculoMapper.toDomain(entity);

        assertThat(entity.getId()).isEqualTo(veiculo.getId());
        assertThat(entity.getMarca()).isEqualTo(veiculo.getMarca());
        assertThat(reconvertido).usingRecursiveComparison().isEqualTo(veiculo);
    }
}
