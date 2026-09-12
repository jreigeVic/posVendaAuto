package com.soat.posvendaauto.sincronizacao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.UUID;

@Component
public class VendaVeiculosClient {

    private final RestClient restClient;
    private final String internalToken;

    public VendaVeiculosClient(RestClient vendaVeiculosRestClient, @Value("${app.internal-token}") String internalToken) {
        this.restClient = vendaVeiculosRestClient;
        this.internalToken = internalToken;
    }

    public boolean sincronizar(TipoEvento tipoEvento, UUID veiculoId, VeiculoSyncPayload payload) {
        try {
            if (tipoEvento == TipoEvento.VEICULO_CRIADO) {
                restClient.post()
                        .uri("/interno/veiculos")
                        .header("X-Internal-Token", internalToken)
                        .body(payload)
                        .retrieve()
                        .toBodilessEntity();
            } else {
                restClient.put()
                        .uri("/interno/veiculos/{id}", veiculoId)
                        .header("X-Internal-Token", internalToken)
                        .body(payload)
                        .retrieve()
                        .toBodilessEntity();
            }
            return true;
        } catch (RestClientException e) {
            return false;
        }
    }
}
