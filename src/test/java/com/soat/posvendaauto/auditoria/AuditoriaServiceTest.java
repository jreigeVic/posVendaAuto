package com.soat.posvendaauto.auditoria;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuditoriaServiceTest {

    @Mock
    private LogAuditoriaRepository repository;

    @Test
    void deveRegistrarSucesso() {
        AuditoriaService service = new AuditoriaService(repository);
        UUID id = UUID.randomUUID();

        service.registrarSucesso("OPERACAO_X", id, "detalhe");

        ArgumentCaptor<LogAuditoria> captor = ArgumentCaptor.forClass(LogAuditoria.class);
        verify(repository).save(captor.capture());
        assertThat(captor.getValue().getResultado()).isEqualTo(Resultado.SUCESSO);
    }

    @Test
    void deveRegistrarErro() {
        AuditoriaService service = new AuditoriaService(repository);
        UUID id = UUID.randomUUID();

        service.registrarErro("OPERACAO_Y", id, "falhou");

        ArgumentCaptor<LogAuditoria> captor = ArgumentCaptor.forClass(LogAuditoria.class);
        verify(repository).save(captor.capture());
        assertThat(captor.getValue().getResultado()).isEqualTo(Resultado.ERRO);
    }
}
