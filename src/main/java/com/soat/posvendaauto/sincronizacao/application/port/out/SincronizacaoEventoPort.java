package com.soat.posvendaauto.sincronizacao.application.port.out;

import com.soat.posvendaauto.sincronizacao.domain.TipoEvento;
import com.soat.posvendaauto.veiculo.domain.Veiculo;

public interface SincronizacaoEventoPort {

    void registrarEvento(Veiculo veiculo, TipoEvento tipoEvento);
}
