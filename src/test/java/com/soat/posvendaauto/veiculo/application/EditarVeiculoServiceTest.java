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
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EditarVeiculoServiceTest {

    @Mock
    private VeiculoRepositoryPort repository;

    @Mock
    private SincronizacaoEventoPort sincronizacaoService;

    @Mock
    private AuditoriaPort auditoriaService;

    private EditarVeiculoService service;

    @BeforeEach
    void setUp() {
        service = new EditarVeiculoService(repository, sincronizacaoService, auditoriaService);
    }

    private DadosVeiculo dados() {
        return new DadosVeiculo("Fiat", "Argo", 2022, "Prata", BigDecimal.valueOf(78900), EstadoConservacao.SEMINOVO);
    }

    @Test
    void deveEditarVeiculoExistenteERegistrarEventoDeAtualizacao() {
        UUID id = UUID.randomUUID();
        Veiculo existente = new Veiculo(id, "Fiat", "Argo", 2022, "Prata", BigDecimal.valueOf(78900),
                EstadoConservacao.SEMINOVO, Instant.now(), Instant.now());
        when(repository.findById(id)).thenReturn(Optional.of(existente));
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        DadosVeiculo dados = new DadosVeiculo("Fiat", "Argo", 2022, "Branco", BigDecimal.valueOf(76900), EstadoConservacao.SEMINOVO);
        Veiculo atualizado = service.editar(id, dados);

        assertThat(atualizado.getCor()).isEqualTo("Branco");
        verify(sincronizacaoService).registrarEvento(eq(atualizado), eq(TipoEvento.VEICULO_ATUALIZADO));
    }

    @Test
    void deveFalharAoEditarVeiculoInexistente() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.editar(id, dados()))
                .isInstanceOf(VeiculoNaoEncontradoException.class);
    }
}
