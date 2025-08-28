package com.itau.transacoes.service;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.DoubleSummaryStatistics;
import java.util.List;

import org.springframework.stereotype.Service;

import com.itau.transacoes.model.Estatistica;
import com.itau.transacoes.model.Transacao;
import com.itau.transacoes.repository.TransacaoRepository;

@Service
public class TransacaoService {

    private final TransacaoRepository repository;
    private static final int SEGUNDOS_STATS = 60;

    public TransacaoService(TransacaoRepository repository) {
        this.repository = repository;
    }

    public void salvarTransacao(Transacao transacao) {
        repository.salvar(transacao);
    }

    public void limparTransacoes() {
        repository.limpar();
    }

    public Estatistica calcularEstatisticas() {
        List<Transacao> todas = repository.listar();
        OffsetDateTime agora = OffsetDateTime.now();

        DoubleSummaryStatistics stats = todas.stream()
                .filter(t -> !t.getDataHora().isAfter(agora) &&
                        Duration.between(t.getDataHora(), agora).getSeconds() <= SEGUNDOS_STATS)
                .mapToDouble(Transacao::getValor)
                .summaryStatistics();

        Estatistica estatistica = new Estatistica();
        estatistica.setCount(stats.getCount());
        estatistica.setSum(stats.getSum());
        estatistica.setAvg(stats.getAverage());
        estatistica.setMin(stats.getCount() > 0 ? stats.getMin() : 0);
        estatistica.setMax(stats.getCount() > 0 ? stats.getMax() : 0);

        return estatistica;
    }
}
