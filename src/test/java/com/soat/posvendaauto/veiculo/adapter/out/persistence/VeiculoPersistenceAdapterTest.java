package com.soat.posvendaauto.veiculo.adapter.out.persistence;

import com.soat.posvendaauto.veiculo.domain.EstadoConservacao;
import com.soat.posvendaauto.veiculo.domain.Veiculo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VeiculoPersistenceAdapterTest {

    @Mock
    private SpringDataVeiculoRepository jpaRepository;

    @Test
    void deveSalvarEMapearDeVoltaParaDomain() {
        VeiculoPersistenceAdapter adapter = new VeiculoPersistenceAdapter(jpaRepository);
        Veiculo veiculo = new Veiculo(UUID.randomUUID(), "Fiat", "Argo", 2022, "Prata",
                BigDecimal.valueOf(78900), EstadoConservacao.SEMINOVO, Instant.now(), Instant.now());
        when(jpaRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Veiculo salvo = adapter.save(veiculo);

        assertThat(salvo).usingRecursiveComparison().isEqualTo(veiculo);
    }

    @Test
    void deveBuscarPorIdEMapearParaDomainQuandoEncontrado() {
        VeiculoPersistenceAdapter adapter = new VeiculoPersistenceAdapter(jpaRepository);
        UUID id = UUID.randomUUID();
        VeiculoJpaEntity entity = new VeiculoJpaEntity(id, "Fiat", "Argo", 2022, "Prata",
                BigDecimal.valueOf(78900), EstadoConservacao.SEMINOVO, Instant.now(), Instant.now());
        when(jpaRepository.findById(id)).thenReturn(Optional.of(entity));

        Optional<Veiculo> encontrado = adapter.findById(id);

        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getId()).isEqualTo(id);
    }

    @Test
    void deveRetornarVazioQuandoNaoEncontrado() {
        VeiculoPersistenceAdapter adapter = new VeiculoPersistenceAdapter(jpaRepository);
        UUID id = UUID.randomUUID();
        when(jpaRepository.findById(id)).thenReturn(Optional.empty());

        assertThat(adapter.findById(id)).isEmpty();
    }
}
