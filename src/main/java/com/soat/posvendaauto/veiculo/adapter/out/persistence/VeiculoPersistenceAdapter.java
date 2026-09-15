package com.soat.posvendaauto.veiculo.adapter.out.persistence;

import com.soat.posvendaauto.veiculo.application.port.out.VeiculoRepositoryPort;
import com.soat.posvendaauto.veiculo.domain.Veiculo;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class VeiculoPersistenceAdapter implements VeiculoRepositoryPort {

    private final SpringDataVeiculoRepository jpaRepository;

    public VeiculoPersistenceAdapter(SpringDataVeiculoRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Veiculo save(Veiculo veiculo) {
        VeiculoJpaEntity salvo = jpaRepository.save(VeiculoMapper.toJpaEntity(veiculo));
        return VeiculoMapper.toDomain(salvo);
    }

    @Override
    public Optional<Veiculo> findById(UUID id) {
        return jpaRepository.findById(id).map(VeiculoMapper::toDomain);
    }
}
