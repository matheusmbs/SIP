package br.com.unipix.api.NumeroService.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import br.com.unipix.api.NumeroService.model.Numero;
import br.com.unipix.api.NumeroService.service.NumeroService;

@Component
public class NumeroScheduler {

    @Autowired
    NumeroService numeroService;

    @Scheduled(cron = "10 0 * * * *")
    public void deletarNumeros() {
        LocalDateTime date = LocalDateTime.now().minusDays(30);
        List<Numero> numeros = numeroService.findNumerosByMaxDate(date);
        numeroService.deleteNumeros(numeros);
    }
}
