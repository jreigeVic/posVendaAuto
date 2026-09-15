package com.soat.posvendaauto.sincronizacao.adapter.out.http;

import com.soat.posvendaauto.sincronizacao.application.port.out.VeiculoSyncPayload;
import com.soat.posvendaauto.sincronizacao.domain.TipoEvento;
import com.soat.posvendaauto.veiculo.domain.EstadoConservacao;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class HttpVendaVeiculosAdapterTest {

    private final VeiculoSyncPayload payload = new VeiculoSyncPayload(
            UUID.randomUUID(), "Fiat", "Argo", 2022, "Prata", BigDecimal.valueOf(78900), EstadoConservacao.SEMINOVO);

    @Test
    void deveRetornarTrueQuandoServicoDeVendaResponde201() {
        RestClient.Builder builder = RestClient.builder().baseUrl("http://venda-veiculos");
        MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
        server.expect(requestTo("http://venda-veiculos/interno/veiculos"))
                .andExpect(method(org.springframework.http.HttpMethod.POST))
                .andExpect(header("X-Internal-Token", "token-teste"))
                .andRespond(withSuccess().contentType(MediaType.APPLICATION_JSON));

        HttpVendaVeiculosAdapter adapter = new HttpVendaVeiculosAdapter(builder.build(), "token-teste");

        boolean resultado = adapter.sincronizar(TipoEvento.VEICULO_CRIADO, payload.id(), payload);

        assertThat(resultado).isTrue();
        server.verify();
    }

    @Test
    void deveRetornarFalseQuandoServicoDeVendaFalha() {
        RestClient.Builder builder = RestClient.builder().baseUrl("http://venda-veiculos");
        MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
        server.expect(requestTo("http://venda-veiculos/interno/veiculos/" + payload.id()))
                .andExpect(method(org.springframework.http.HttpMethod.PUT))
                .andRespond(withServerError());

        HttpVendaVeiculosAdapter adapter = new HttpVendaVeiculosAdapter(builder.build(), "token-teste");

        boolean resultado = adapter.sincronizar(TipoEvento.VEICULO_ATUALIZADO, payload.id(), payload);

        assertThat(resultado).isFalse();
    }
}
