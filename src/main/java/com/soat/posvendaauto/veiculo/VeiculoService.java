package com.soat.posvendaauto.veiculo;

import com.soat.posvendaauto.auditoria.AuditoriaService;
import com.soat.posvendaauto.sincronizacao.SincronizacaoService;
import com.soat.posvendaauto.sincronizacao.TipoEvento;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class VeiculoService {

    private final VeiculoRepository repository;
    private final SincronizacaoService sincronizacaoService;
    private final AuditoriaService auditoriaService;

    public VeiculoService(VeiculoRepository repository, SincronizacaoService sincronizacaoService, AuditoriaService auditoriaService) {
        this.repository = repository;
        this.sincronizacaoService = sincronizacaoService;
        this.auditoriaService = auditoriaService;
    }

    @Transactional
    public Veiculo cadastrar(VeiculoRequest request) {
        Instant agora = Instant.now();
        Veiculo veiculo = new Veiculo(null, request.marca(), request.modelo(), request.ano(), request.cor(),
                request.preco(), request.estadoConservacao(), agora, agora);
        Veiculo salvo = repository.save(veiculo);

        sincronizacaoService.registrarEvento(salvo, TipoEvento.VEICULO_CRIADO);
        auditoriaService.registrarSucesso("CADASTRAR_VEICULO", salvo.getId(), "Veículo cadastrado");
        return salvo;
    }

    @Transactional
    public Veiculo editar(UUID id, VeiculoRequest request) {
        Veiculo veiculo = repository.findById(id)
                .orElseThrow(() -> new VeiculoNaoEncontradoException(id));

        veiculo.setMarca(request.marca());
        veiculo.setModelo(request.modelo());
        veiculo.setAno(request.ano());
        veiculo.setCor(request.cor());
        veiculo.setPreco(request.preco());
        veiculo.setEstadoConservacao(request.estadoConservacao());
        veiculo.setAtualizadoEm(Instant.now());

        Veiculo salvo = repository.save(veiculo);

        sincronizacaoService.registrarEvento(salvo, TipoEvento.VEICULO_ATUALIZADO);
        auditoriaService.registrarSucesso("EDITAR_VEICULO", salvo.getId(), "Veículo atualizado");
        return salvo;
    }
}
