package br.com.unipix.api.NumeroService.scheduler;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.data.domain.PageRequest;

import br.com.unipix.api.NumeroService.model.Numero;
import br.com.unipix.api.NumeroService.service.NumeroService;
import br.com.unipix.api.NumeroService.service.SipService;
import java.time.LocalDateTime;

@Component
public class SipScheduler {

    @Autowired
    NumeroService numeroService;

    @Autowired
    SipService sipService;

    @Scheduled(cron = "*/30 * * * * *")
    public void ligar() throws IOException, InterruptedException, ExecutionException {
        Long quantidadeEmProcessamento = this.numeroService.countNumerosByStatusProcessamento("Em processamento");
        Long quantidadeProcessar = 500 - quantidadeEmProcessamento;
        if (quantidadeProcessar > 0) {
            PageRequest pageable = PageRequest.of(0, quantidadeEmProcessamento.intValue());
            List<Numero> numeros = this.numeroService.findNumerosByStatusProcessamento("Aguardando Processamento",
                    pageable);

            this.sipService.ligar(numeros);

            for (Numero numero : numeros) {
                LocalDateTime today = LocalDateTime.now();
                numero.setDataProcessamento(today);
                numero.setStatusProcessamento("Em processamento");
            }

            this.numeroService.saveAll(numeros);

        }
    }

    @Scheduled(cron = "*/30 * * * * *")
    public void reprocessarLigacoes() throws IOException, InterruptedException, ExecutionException {
        LocalDateTime date = LocalDateTime.now().minusMinutes(5);
        List<Numero> numeros = this.numeroService.findNumerosByStatusProcessamentoAndDate("Em processamento", date);
        for (Numero numero : numeros) {
            if (numero.getQuantidadeReprocessamento() <= 3) {
                numero.setQuantidadeReprocessamento(numero.getQuantidadeReprocessamento() + 1);
                numero.setDataProcessamento(null);
                numero.setStatusProcessamento("Aguardando Processamento");
            } else {
                LocalDateTime today = LocalDateTime.now();
                numero.setDataFinalizacao(today);
                numero.setStatusProcessamento("Processado");
                numero.setStatusCode(-1);
            }
        }

        this.numeroService.saveAll(numeros);

    }

}
