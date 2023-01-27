package br.com.unipix.api.NumeroService.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.com.unipix.api.NumeroService.SIP.SipCallThread;
import br.com.unipix.api.NumeroService.SIP.SipNotification;
import br.com.unipix.api.NumeroService.model.Numero;
import webphone.webphone;

@Service
public class SipService {
    private webphone webphoneobj;
    private SipNotification sipNotification = null;
    private Integer line = 1;

    public List<Numero> processCall(List<Numero> numeros) throws IOException, InterruptedException, ExecutionException {
        List<Numero> numerosProcessados = new ArrayList<Numero>();
        List<Numero> numerosChamados = this.call(numeros);
        List<Numero> numerosValidos = numerosChamados.stream().filter(n -> n.getStatusCode() != 0)
                .collect(Collectors.toList());
        List<Numero> numeroReprocessamento = numerosChamados.stream().filter(n -> n.getStatusCode() == 0)
                .collect(Collectors.toList());

        if (numeroReprocessamento.size() > 0) {
            numerosValidos.addAll(this.processCall(numeroReprocessamento));
        }

        numerosProcessados.addAll(numerosValidos);
        return numerosProcessados;
    }

    public List<Numero> call(List<Numero> numeros) throws IOException,
            InterruptedException, ExecutionException {
        this.sipStart();


        Thread.sleep(2000);
        List<Numero> numerosProcessados = new ArrayList<Numero>();
        int numThreads = numeros.size();
        ExecutorService executor = Executors.newFixedThreadPool(500);
        List<Future<Numero>> mainThreads = new ArrayList<Future<Numero>>();

        for (int i = 0; i < numThreads; i++) {
            Future<Numero> mainThread = executor
                    .submit(new SipCallThread(webphoneobj, sipNotification, numeros.get(i), i));
            mainThreads.add(mainThread);
            this.line++;
        }

        // // Fecha o executor e espera todas as tarefas terminarem
        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

        for (Future nf : mainThreads) {
            Numero numero = (Numero) nf.get();
            numerosProcessados.add(numero);
        }

        this.sipDisconect();
        return numerosProcessados;
    }

    private void sipStart() throws IOException {
        URL whatismyip = new URL("http://checkip.amazonaws.com");
        BufferedReader in = new BufferedReader(new InputStreamReader(
                whatismyip.openStream()));
        String ip = in.readLine();
        webphoneobj = new webphone();
        // Alguns dos parâmetros utilizados nesta função foram passados somente como
        // teste, e podem ser ajustados ou removidos conforme necessário.
        webphoneobj.API_SetParameter("loglevel", "1");
        webphoneobj.API_SetParameter("logtoconsole", "1");
        webphoneobj.API_SetParameter("systemexit", "2");
        webphoneobj.API_SetParameter("syncvoicerec", "0");
        webphoneobj.API_SetParameter("useaudiodevicerecord", "false");
        webphoneobj.API_SetParameter("useaudiodeviceplayback", "false");
        webphoneobj.API_SetParameter("mediatimeout", "0");
        webphoneobj.API_SetParameter("defsetmuted", "3");
        webphoneobj.API_SetParameter("aec", "0");
        webphoneobj.API_SetParameter("serveraddress", "177.53.17.120:5060");
        webphoneobj.API_SetParameter("username", "55111185744186");
        webphoneobj.API_SetParameter("password", "W5UF6SpQajf6zR");
        webphoneobj.API_SetParameter("proxyaddress", ip);
        webphoneobj.API_SetParameter("register", "1");
        webphoneobj.API_SetParameter("registerinterval", "86400");
        webphoneobj.API_SetParameter("needunregister", "false");
        webphoneobj.API_SetParameter("contactaddressfailback", "2");
        webphoneobj.API_SetParameter("events", "0");
        webphoneobj.API_Start();
        this.sipNotification = new SipNotification(webphoneobj);
        this.sipNotification.start();
    }

    private void sipDisconect() {
        webphoneobj.API_Unregister();
        webphoneobj.API_Stop();
        webphoneobj.API_Exit();
        this.webphoneobj = null;
        this.sipNotification.Stop();
        this.sipNotification = null;
    }
}
