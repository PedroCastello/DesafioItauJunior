package com.itau.transacoes.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.itau.transacoes.model.Transacao;

@Repository
public class TransacaoRepository {

    private final List<Transacao> transacoes = new ArrayList<>();

    public void salvar(Transacao transacao) {
        transacoes.add(transacao);
    }

    public List<Transacao> listar() {
        return new ArrayList<>(transacoes);
    }

    public void limpar() {
        transacoes.clear();
    }
}
