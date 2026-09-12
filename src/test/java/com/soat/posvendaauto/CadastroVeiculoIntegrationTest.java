package com.soat.posvendaauto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CadastroVeiculoIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String PAYLOAD = """
            {
              "marca": "Fiat",
              "modelo": "Argo",
              "ano": 2022,
              "cor": "Prata",
              "preco": 78900.00,
              "estadoConservacao": "SEMINOVO"
            }
            """;

    @Test
    void deveRejeitarCadastroSemAutenticacao() throws Exception {
        mockMvc.perform(post("/veiculos").contentType(MediaType.APPLICATION_JSON).content(PAYLOAD))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deveCadastrarVeiculoComUsuarioAutenticado() throws Exception {
        mockMvc.perform(post("/veiculos")
                        .with(httpBasic("operador", "teste123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PAYLOAD))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.marca").value("Fiat"))
                .andExpect(jsonPath("$.estadoConservacao").value("SEMINOVO"));
    }

    @Test
    void deveRetornarNotFoundAoEditarVeiculoInexistente() throws Exception {
        mockMvc.perform(put("/veiculos/" + UUID.randomUUID())
                        .with(httpBasic("operador", "teste123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PAYLOAD))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveRetornarBadRequestParaPayloadInvalido() throws Exception {
        mockMvc.perform(post("/veiculos")
                        .with(httpBasic("operador", "teste123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }
}
