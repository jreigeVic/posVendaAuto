package com.soat.posvendaauto.sincronizacao.adapter.in.scheduler;

import com.soat.posvendaauto.sincronizacao.application.port.in.ReenviarEventosPendentesUseCase;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SincronizacaoJob {

    private final ReenviarEventosPendentesUseCase reenviarEventosPendentesUseCase;

    public SincronizacaoJob(ReenviarEventosPendentesUseCase reenviarEventosPendentesUseCase) {
        this.reenviarEventosPendentesUseCase = reenviarEventosPendentesUseCase;
    }

    @Scheduled(fixedDelayString = "${sincronizacao.job.intervalo-ms:5000}")
    public void reenviarPendentes() {
        reenviarEventosPendentesUseCase.reenviarPendentes();
    }
}
