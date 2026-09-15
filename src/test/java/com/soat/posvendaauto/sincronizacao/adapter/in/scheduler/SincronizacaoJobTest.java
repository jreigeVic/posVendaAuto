package com.soat.posvendaauto.sincronizacao.adapter.in.scheduler;

import com.soat.posvendaauto.sincronizacao.application.port.in.ReenviarEventosPendentesUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SincronizacaoJobTest {

    @Mock
    private ReenviarEventosPendentesUseCase useCase;

    @Test
    void deveDelegarParaOUseCaseAoDisparar() {
        SincronizacaoJob job = new SincronizacaoJob(useCase);

        job.reenviarPendentes();

        verify(useCase).reenviarPendentes();
    }
}
