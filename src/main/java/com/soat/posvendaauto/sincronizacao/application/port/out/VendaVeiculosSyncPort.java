package com.soat.posvendaauto.sincronizacao.application.port.out;

import com.soat.posvendaauto.sincronizacao.domain.TipoEvento;

import java.util.UUID;

public interface VendaVeiculosSyncPort {

    boolean sincronizar(TipoEvento tipoEvento, UUID veiculoId, VeiculoSyncPayload payload);
}
