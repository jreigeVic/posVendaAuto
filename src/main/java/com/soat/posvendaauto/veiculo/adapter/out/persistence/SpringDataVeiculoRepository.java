package com.soat.posvendaauto.veiculo.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataVeiculoRepository extends JpaRepository<VeiculoJpaEntity, UUID> {
}
