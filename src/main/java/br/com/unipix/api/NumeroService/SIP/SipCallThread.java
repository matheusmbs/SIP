package br.com.unipix.api.NumeroService.SIP;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.apache.commons.lang3.math.NumberUtils;

import br.com.unipix.api.NumeroService.model.Numero;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

import webphone.webphone;

public class SipCallThread implements Callable<Numero> {
    webphone webphoneobj;
    Integer line;
    Numero numero;
    SipNotification sipNotification;
    List<String> notification = new ArrayList<>();

    public SipCallThread(webphone webphoneobj, SipNotification sipNotification, Numero numero, Integer line)
            throws IOException {
        this.webphoneobj = webphoneobj;
        this.numero = numero;
        this.line = line;
        this.sipNotification = sipNotification;
    }

    @Override
    public Numero call() throws IOException {
        long startTime = System.currentTimeMillis();
        this.webphoneobj.API_Call(this.line, this.numero.getNumero());

        if(this.line % 500 == 0){
            System.out.println("Executando números " + this.line);
        }

        // Loop para pegar os detalhes da ligação
        while (true) {
            try {
                long endTime = System.currentTimeMillis();
                long elapsedTime = endTime - startTime;
                long seconds = TimeUnit.MILLISECONDS.toSeconds(elapsedTime);
                if (seconds > 22) {
                    this.sipMensagem();
                    break;
                }
                Boolean notification = this.sipNotification.getNotifications().stream()
                        .anyMatch(s -> s.contains(this.numero.getNumero()));
                Boolean cdr = this.sipNotification.getCDR().stream().anyMatch(s -> s.contains(this.numero.getNumero()));

                if (notification && cdr) {
                    this.sipMensagem();
                    break;
                }
            } catch (Exception e) {

            }

        }
        return numero;
    }

    public void sipMensagem() throws IOException {
        String sipMessagem = webphoneobj.API_GetSIPMessage(this.line, 0, 2);
        if (sipMessagem.contains("To: <sip:" + this.numero.getNumero())) {
            String[] sipMensagemLines = sipMessagem.split("\n");
            String statusLine = sipMensagemLines[0];
            String statusCode = statusLine.split(" ")[1];
            String callId = sipMensagemLines[4].replace("Call-ID: ", "").replace("\r",
                    "");
            LocalDateTime today = LocalDateTime.now();
            numero.setDataProcessamento(today);
            if (NumberUtils.isDigits(statusCode)) {
                numero.setStatusCode(Integer.parseInt(statusCode));
            }
            numero.setCallId(callId);
            FileWriter fileWriter = new FileWriter("numeros.txt", true);
            BufferedWriter writer = new BufferedWriter(fileWriter);
            if (numero.getStatusCode() != 0) {
                String text = "\n" + numero.getNumero() + numero.getStatusSMS() + "," + numero.getStatusCode();
                writer.write(text);
            }
            writer.close();
        }
    }
}
