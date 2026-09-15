package com.soat.posvendaauto.veiculo.adapter.in.web;

import com.soat.posvendaauto.veiculo.application.port.in.CadastrarVeiculoUseCase;
import com.soat.posvendaauto.veiculo.application.port.in.DadosVeiculo;
import com.soat.posvendaauto.veiculo.application.port.in.EditarVeiculoUseCase;
import com.soat.posvendaauto.veiculo.domain.Veiculo;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/veiculos")
public class VeiculoController {

    private final CadastrarVeiculoUseCase cadastrarVeiculoUseCase;
    private final EditarVeiculoUseCase editarVeiculoUseCase;

    public VeiculoController(CadastrarVeiculoUseCase cadastrarVeiculoUseCase, EditarVeiculoUseCase editarVeiculoUseCase) {
        this.cadastrarVeiculoUseCase = cadastrarVeiculoUseCase;
        this.editarVeiculoUseCase = editarVeiculoUseCase;
    }

    @PostMapping
    public ResponseEntity<VeiculoResponse> cadastrar(@Valid @RequestBody VeiculoRequest request) {
        Veiculo veiculo = cadastrarVeiculoUseCase.cadastrar(paraDados(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(VeiculoResponse.de(veiculo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VeiculoResponse> editar(@PathVariable UUID id, @Valid @RequestBody VeiculoRequest request) {
        Veiculo veiculo = editarVeiculoUseCase.editar(id, paraDados(request));
        return ResponseEntity.ok(VeiculoResponse.de(veiculo));
    }

    private DadosVeiculo paraDados(VeiculoRequest request) {
        return new DadosVeiculo(request.marca(), request.modelo(), request.ano(), request.cor(),
                request.preco(), request.estadoConservacao());
    }
}
