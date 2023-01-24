package br.com.unipix.api.NumeroService.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import br.com.unipix.api.NumeroService.SIP.SipCallThread;
import br.com.unipix.api.NumeroService.model.Numero;

@Service
public class SipService {
    


    public List<Numero> call(List<Numero> numeros) throws IOException, InterruptedException {
        int numThreads = numeros.size();
        ExecutorService executor = Executors.newFixedThreadPool(500);
        List<SipCallThread> mainThreads = new ArrayList<SipCallThread>();
        List<Numero> numerosProcessados = new ArrayList<Numero>();

        // Envia as tarefas para o executor
        for (int i = 0; i < numThreads; i++) {
            SipCallThread mainThread = new SipCallThread(numeros.get(i), i);
            mainThreads.add(mainThread);
            executor.execute(mainThread);
        }

        // Fecha o executor
        executor.shutdown();

        // Espera todas as tarefas terminarem
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (SipCallThread mt : mainThreads) {
            numerosProcessados.add(mt.getNumero());
        }
        return numerosProcessados;
    }
}
