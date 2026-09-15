package com.soat.posvendaauto.veiculo.application;

import com.soat.posvendaauto.auditoria.application.port.out.AuditoriaPort;
import com.soat.posvendaauto.sincronizacao.application.port.out.SincronizacaoEventoPort;
import com.soat.posvendaauto.sincronizacao.domain.TipoEvento;
import com.soat.posvendaauto.veiculo.application.port.in.DadosVeiculo;
import com.soat.posvendaauto.veiculo.application.port.in.EditarVeiculoUseCase;
import com.soat.posvendaauto.veiculo.application.port.out.VeiculoRepositoryPort;
import com.soat.posvendaauto.veiculo.domain.Veiculo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class EditarVeiculoService implements EditarVeiculoUseCase {

    private final VeiculoRepositoryPort repository;
    private final SincronizacaoEventoPort sincronizacaoService;
    private final AuditoriaPort auditoriaService;

    public EditarVeiculoService(VeiculoRepositoryPort repository, SincronizacaoEventoPort sincronizacaoService,
                                 AuditoriaPort auditoriaService) {
        this.repository = repository;
        this.sincronizacaoService = sincronizacaoService;
        this.auditoriaService = auditoriaService;
    }

    @Override
    @Transactional
    public Veiculo editar(UUID id, DadosVeiculo dados) {
        Veiculo veiculo = repository.findById(id)
                .orElseThrow(() -> new VeiculoNaoEncontradoException(id));

        veiculo.setMarca(dados.marca());
        veiculo.setModelo(dados.modelo());
        veiculo.setAno(dados.ano());
        veiculo.setCor(dados.cor());
        veiculo.setPreco(dados.preco());
        veiculo.setEstadoConservacao(dados.estadoConservacao());
        veiculo.setAtualizadoEm(Instant.now());

        Veiculo salvo = repository.save(veiculo);

        sincronizacaoService.registrarEvento(salvo, TipoEvento.VEICULO_ATUALIZADO);
        auditoriaService.registrarSucesso("EDITAR_VEICULO", salvo.getId(), "Veículo atualizado");
        return salvo;
    }
}
