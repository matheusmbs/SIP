package br.com.unipix.api.NumeroService.scheduler;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import br.com.unipix.api.NumeroService.model.Numero;
import br.com.unipix.api.NumeroService.service.NumeroService;
import br.com.unipix.api.NumeroService.service.SipService;

@Component
public class SipScheduler {
    @Autowired
    SipService sipService;

    @Autowired
    NumeroService numeroService;

    @Scheduled(cron = "1 * * * * *")
    public void processaNumeros() throws IOException, InterruptedException {
        List<Numero> numeros = numeroService.findNumerosByStatusCode(0);
        if (numeros.size() > 0) {
            numeros = sipService.call(numeros);
            numeroService.saveAll(numeros);
        }
    }
}
