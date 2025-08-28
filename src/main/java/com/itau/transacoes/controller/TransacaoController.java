package com.itau.transacoes.controller;

import java.time.OffsetDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.itau.transacoes.model.Estatistica;
import com.itau.transacoes.model.Transacao;
import com.itau.transacoes.service.TransacaoService;

import jakarta.validation.Valid;

@RestController
public class TransacaoController {

    private final TransacaoService service;

    public TransacaoController(TransacaoService service) {
        this.service = service;
    }

    @PostMapping("/transacao")
    public ResponseEntity<Void> criarTransacao(@RequestBody @Valid Transacao transacao) {
        
        if (transacao.getDataHora() == null) {
            transacao.setDataHora(OffsetDateTime.now());
        }

        if (transacao.getValor() < 0 || transacao.getDataHora().isAfter(OffsetDateTime.now())) {
            return ResponseEntity.unprocessableEntity().build();
        }

        service.salvarTransacao(transacao);
        return ResponseEntity.status(HttpStatus.CREATED).build(); 
    }

    @DeleteMapping("/transacao")
    public ResponseEntity<Void> limparTransacoes() {
        service.limparTransacoes();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/estatistica")
    public ResponseEntity<Estatistica> estatisticas() {
        Estatistica estatistica = service.calcularEstatisticas();
        return ResponseEntity.ok(estatistica);
    }
}