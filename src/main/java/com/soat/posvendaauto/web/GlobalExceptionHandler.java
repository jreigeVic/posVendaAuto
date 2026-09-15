package com.soat.posvendaauto.web;

import com.soat.posvendaauto.auditoria.application.port.out.AuditoriaPort;
import com.soat.posvendaauto.veiculo.application.VeiculoNaoEncontradoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final AuditoriaPort auditoriaService;

    public GlobalExceptionHandler(AuditoriaPort auditoriaService) {
        this.auditoriaService = auditoriaService;
    }

    @ExceptionHandler(VeiculoNaoEncontradoException.class)
    public ResponseEntity<ErroResponse> tratar(VeiculoNaoEncontradoException ex) {
        auditoriaService.registrarErro("VEICULO_NAO_ENCONTRADO", ex.getVeiculoId(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErroResponse(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> tratar(MethodArgumentNotValidException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErroResponse("Requisição inválida"));
    }
}
