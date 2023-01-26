package br.com.unipix.api.NumeroService.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.apache.commons.lang3.math.NumberUtils;
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

    // public List<Numero> call(List<Numero> numeros) throws IOException,
    // InterruptedException {
    // URL whatismyip = new URL("http://checkip.amazonaws.com");
    // BufferedReader in = new BufferedReader(new InputStreamReader(
    // whatismyip.openStream()));
    // String ip = in.readLine();
    // int numThreads = numeros.size();
    // ExecutorService executor = Executors.newFixedThreadPool(5000);
    // List<SipCallThread> mainThreads = new ArrayList<SipCallThread>();
    // List<Numero> numerosProcessados = new ArrayList<Numero>();

    // // Envia as tarefas para o executor
    // for (int i = 0; i < numThreads; i++) {
    // SipCallThread mainThread = new SipCallThread(numeros.get(i), i, ip);
    // mainThreads.add(mainThread);
    // }

    // Thread.sleep(3000);

    // for (SipCallThread mainThread : mainThreads) {
    // executor.execute(mainThread);
    // }

    // // Fecha o executor
    // executor.shutdown();

    // // Espera todas as tarefas terminarem
    // try {
    // executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
    // } catch (InterruptedException e) {
    // e.printStackTrace();
    // }

    // List<String> fileText = new ArrayList();
    // Integer i = 0;
    // for (SipCallThread mt : mainThreads) {
    // numerosProcessados.add(mt.getNumero());

    // String text = mt.getNumero().getNumero() + ";" +
    // mt.getNumero().getStatusCode() + ";"
    // + mt.getNumero().getCallId();
    // text = "\n" + text;

    // i++;
    // fileText.add(text);
    // }
    // try (FileWriter fileWriter = new FileWriter("numeros.txt", true);
    // BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
    // bufferedWriter.write(String.join("", fileText));
    // } catch (IOException e) {
    // System.err.println("Erro ao gravar arquivo " + "numeros.txt" + ": " +
    // e.getMessage());
    // }
    // return numerosProcessados;
    // }

    public List<Numero> call(List<Numero> numeros) throws IOException,
            InterruptedException {
        this.sipStart();
        Thread.sleep(2000);
        List<Numero> numerosProcessados = new ArrayList<Numero>();
        int numThreads = numeros.size();
        ExecutorService executor = Executors.newFixedThreadPool(500);
        List<SipCallThread> mainThreads = new ArrayList<SipCallThread>();

        for (int i = 0; i < numThreads; i++) {
            SipCallThread mainThread = new SipCallThread(webphoneobj, numeros.get(i), i);
            mainThreads.add(mainThread);
            executor.execute(mainThread);
            this.line++;
        }

        // // Fecha o executor
        executor.shutdown();

        // Espera todas as tarefas terminarem
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (SipCallThread mt : mainThreads) {
            try (FileWriter fileWriter = new FileWriter("logsNumeros/" + mt.getNumero().getNumero() + ".txt", false);
                    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
                bufferedWriter.write(String.join("\n", mt.getNotification()));
                // fileWriter.close();
            } catch (IOException e) {
                System.err.println("Erro ao gravar arquivo " + "cdr.txt" + ": " +
                        e.getMessage());
            }
            numerosProcessados.add(mt.getNumero());
        }

        // long startTime = System.currentTimeMillis();
        // while (true) {
        // Thread.sleep(1000);
        // long endTime = System.currentTimeMillis();
        // long elapsedTime = endTime - startTime;
        // long seconds = TimeUnit.MILLISECONDS.toSeconds(elapsedTime);
        // if (seconds > 10) {
        // this.webphoneobj.API_Hangup(-1);
        // }

        // if (seconds > 15 || this.sipNotification.getNotifications().size() >=
        // numeros.size()) {
        // break;
        // }
        // }

        // List<String> sipNotifications = this.sipNotification.getNotifications();
        // for (String notification : sipNotifications) {
        // String[] nArr = notification.split(",");
        // String linha = nArr[1];
        // String numeroNotificação = nArr[3];
        // Numero numero = numeros.stream().filter(obj ->
        // obj.getNumero().equals(numeroNotificação.trim())).findFirst()
        // .orElse(null);

        // if (numero != null) {
        // String sipMessagem = webphoneobj.API_GetSIPMessage(Integer.parseInt(linha),
        // 0, 2);
        // LocalDateTime today = LocalDateTime.now();
        // numero.setDataProcessamento(today);
        // if (sipMessagem.contains("To: <sip:" + numeroNotificação)) {
        // String[] sipMensagemLines = sipMessagem.split("\n");
        // String statusLine = sipMensagemLines[0];
        // String statusCode = statusLine.split(" ")[1];
        // String callId = sipMensagemLines[4].replace("Call-ID: ", "").replace("\r",
        // "");
        // if (NumberUtils.isDigits(statusCode)) {
        // numero.setStatusCode(Integer.parseInt(statusCode));
        // }
        // numero.setCallId(callId);
        // }
        // numerosProcessados.add(numero);
        // }
        // }

        // try (FileWriter fileWriter = new FileWriter("cdr.txt", true);
        // BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
        // bufferedWriter.write(String.join("\n", this.sipNotification.getCDR()));
        // } catch (IOException e) {
        // System.err.println("Erro ao gravar arquivo " + "cdr.txt" + ": " +
        // e.getMessage());
        // }

        // List<String> fileText = numeros.stream()
        // .map(n -> n.getNumero() + ";" + n.getStatusCode() + ";" + n.getCallId() +
        // "\n")
        // .collect(Collectors.toList());

        // try (FileWriter fileWriter = new FileWriter("numeros.txt", true);
        // BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
        // bufferedWriter.write(String.join("", fileText));
        // } catch (IOException e) {
        // System.err.println("Erro ao gravar arquivo " + "numeros.txt" + ": " +
        // e.getMessage());
        // }

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
