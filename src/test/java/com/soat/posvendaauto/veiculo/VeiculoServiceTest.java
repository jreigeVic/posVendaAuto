package com.soat.posvendaauto.veiculo;

import com.soat.posvendaauto.auditoria.AuditoriaService;
import com.soat.posvendaauto.sincronizacao.SincronizacaoService;
import com.soat.posvendaauto.sincronizacao.TipoEvento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VeiculoServiceTest {

    @Mock
    private VeiculoRepository repository;

    @Mock
    private SincronizacaoService sincronizacaoService;

    @Mock
    private AuditoriaService auditoriaService;

    private VeiculoService service;

    @BeforeEach
    void setUp() {
        service = new VeiculoService(repository, sincronizacaoService, auditoriaService);
    }

    private VeiculoRequest request() {
        return new VeiculoRequest("Fiat", "Argo", 2022, "Prata", BigDecimal.valueOf(78900), EstadoConservacao.SEMINOVO);
    }

    @Test
    void deveCadastrarVeiculoERegistrarEventoDeSincronizacao() {
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Veiculo veiculo = service.cadastrar(request());

        assertThat(veiculo.getMarca()).isEqualTo("Fiat");
        assertThat(veiculo.getCriadoEm()).isNotNull();
        verify(sincronizacaoService).registrarEvento(eq(veiculo), eq(TipoEvento.VEICULO_CRIADO));
    }

    @Test
    void deveEditarVeiculoExistenteERegistrarEventoDeAtualizacao() {
        UUID id = UUID.randomUUID();
        Veiculo existente = new Veiculo(id, "Fiat", "Argo", 2022, "Prata", BigDecimal.valueOf(78900),
                EstadoConservacao.SEMINOVO, java.time.Instant.now(), java.time.Instant.now());
        when(repository.findById(id)).thenReturn(Optional.of(existente));
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        VeiculoRequest request = new VeiculoRequest("Fiat", "Argo", 2022, "Branco", BigDecimal.valueOf(76900), EstadoConservacao.SEMINOVO);
        Veiculo atualizado = service.editar(id, request);

        assertThat(atualizado.getCor()).isEqualTo("Branco");
        verify(sincronizacaoService).registrarEvento(eq(atualizado), eq(TipoEvento.VEICULO_ATUALIZADO));
    }

    @Test
    void deveFalharAoEditarVeiculoInexistente() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.editar(id, request()))
                .isInstanceOf(VeiculoNaoEncontradoException.class);
    }
}
