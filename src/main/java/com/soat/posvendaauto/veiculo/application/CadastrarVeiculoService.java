package com.soat.posvendaauto.veiculo.application;

import com.soat.posvendaauto.auditoria.application.port.out.AuditoriaPort;
import com.soat.posvendaauto.sincronizacao.application.port.out.SincronizacaoEventoPort;
import com.soat.posvendaauto.sincronizacao.domain.TipoEvento;
import com.soat.posvendaauto.veiculo.application.port.in.CadastrarVeiculoUseCase;
import com.soat.posvendaauto.veiculo.application.port.in.DadosVeiculo;
import com.soat.posvendaauto.veiculo.application.port.out.VeiculoRepositoryPort;
import com.soat.posvendaauto.veiculo.domain.Veiculo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class CadastrarVeiculoService implements CadastrarVeiculoUseCase {

    private final VeiculoRepositoryPort repository;
    private final SincronizacaoEventoPort sincronizacaoService;
    private final AuditoriaPort auditoriaService;

    public CadastrarVeiculoService(VeiculoRepositoryPort repository, SincronizacaoEventoPort sincronizacaoService,
                                    AuditoriaPort auditoriaService) {
        this.repository = repository;
        this.sincronizacaoService = sincronizacaoService;
        this.auditoriaService = auditoriaService;
    }

    @Override
    @Transactional
    public Veiculo cadastrar(DadosVeiculo dados) {
        Instant agora = Instant.now();
        Veiculo veiculo = new Veiculo(null, dados.marca(), dados.modelo(), dados.ano(), dados.cor(),
                dados.preco(), dados.estadoConservacao(), agora, agora);
        Veiculo salvo = repository.save(veiculo);

        sincronizacaoService.registrarEvento(salvo, TipoEvento.VEICULO_CRIADO);
        auditoriaService.registrarSucesso("CADASTRAR_VEICULO", salvo.getId(), "Veículo cadastrado");
        return salvo;
    }
}
