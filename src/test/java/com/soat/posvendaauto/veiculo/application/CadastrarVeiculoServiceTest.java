package com.soat.posvendaauto.veiculo.application;

import com.soat.posvendaauto.auditoria.application.port.out.AuditoriaPort;
import com.soat.posvendaauto.sincronizacao.application.port.out.SincronizacaoEventoPort;
import com.soat.posvendaauto.sincronizacao.domain.TipoEvento;
import com.soat.posvendaauto.veiculo.application.port.in.DadosVeiculo;
import com.soat.posvendaauto.veiculo.application.port.out.VeiculoRepositoryPort;
import com.soat.posvendaauto.veiculo.domain.EstadoConservacao;
import com.soat.posvendaauto.veiculo.domain.Veiculo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CadastrarVeiculoServiceTest {

    @Mock
    private VeiculoRepositoryPort repository;

    @Mock
    private SincronizacaoEventoPort sincronizacaoService;

    @Mock
    private AuditoriaPort auditoriaService;

    private CadastrarVeiculoService service;

    @BeforeEach
    void setUp() {
        service = new CadastrarVeiculoService(repository, sincronizacaoService, auditoriaService);
    }

    private DadosVeiculo dados() {
        return new DadosVeiculo("Fiat", "Argo", 2022, "Prata", BigDecimal.valueOf(78900), EstadoConservacao.SEMINOVO);
    }

    @Test
    void deveCadastrarVeiculoERegistrarEventoDeSincronizacao() {
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Veiculo veiculo = service.cadastrar(dados());

        assertThat(veiculo.getMarca()).isEqualTo("Fiat");
        assertThat(veiculo.getCriadoEm()).isNotNull();
        verify(sincronizacaoService).registrarEvento(eq(veiculo), eq(TipoEvento.VEICULO_CRIADO));
        verify(auditoriaService).registrarSucesso(eq("CADASTRAR_VEICULO"), any(), any());
    }
}
