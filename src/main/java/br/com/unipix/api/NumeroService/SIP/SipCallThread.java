package br.com.unipix.api.NumeroService.SIP;

import java.time.LocalDateTime;
import br.com.unipix.api.NumeroService.model.Numero;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.math.NumberUtils;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;

import webphone.webphone;

public class SipCallThread implements Runnable {
    webphone webphoneobj = new webphone();
    Integer line;
    Numero numero;

    public SipCallThread(Numero numero, Integer line) throws IOException {
        this.numero = numero;
        this.line = line;
        this.sipConect();
    }

    @Override
    public void run() {
        Boolean inLoop = true;
        List<String> arrDetailLog = new ArrayList();
        arrDetailLog.add(this.numero.getNumero() + "\n");
        String detailAux = "";
        long startTime = System.currentTimeMillis();
        while (inLoop) {
            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;
            long seconds = TimeUnit.MILLISECONDS.toSeconds(elapsedTime);


            if (seconds > 15) {
                arrDetailLog.add(this.webphoneobj.API_GetLineDetails(this.line));
                LocalDateTime today = LocalDateTime.now();
                numero.setDataProcessamento(today);
                numero.setStatusCode(1000);
                inLoop = false;
            }

            String statusCall = this.webphoneobj.API_GetLineDetails(this.line);
            if (statusCall.contains(this.numero.getNumero())) {
                if (!detailAux.equals(statusCall)) {
                    detailAux = statusCall;
                    arrDetailLog.add(statusCall);
                }

                if (statusCall.contains("Finished")) {
                    String sipMessagem = webphoneobj.API_GetSIPMessage(this.line, 0, 2);
                    arrDetailLog.add("\n" + sipMessagem);
                    if (sipMessagem.contains("To: <sip:" + this.numero.getNumero())) {

                        String[] sipMensagemLines = sipMessagem.split("\n");
                        String statusLine = sipMensagemLines[0];
                        String statusCode = statusLine.split(" ")[1];
                        String callId = sipMensagemLines[4];
                        LocalDateTime today = LocalDateTime.now();
                        numero.setDataProcessamento(today);
                        if (NumberUtils.isDigits(statusCode)) {
                            numero.setStatusCode(Integer.parseInt(statusCode));
                        }
                        numero.setCallId(callId);
                    }
                    inLoop = false;
                    this.sipDisconect();

                }
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

    public Numero getNumero() {
        return this.numero;
    }

    private void sipConect() throws IOException {
        URL whatismyip = new URL("http://checkip.amazonaws.com");
        BufferedReader in = new BufferedReader(new InputStreamReader(
                whatismyip.openStream()));
        String ip = in.readLine();
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
        webphoneobj.API_SetParameter("callto", this.getNumero().getNumero());
        webphoneobj.API_SetParameter("autocall", "true");
        webphoneobj.API_Start();
        this.webphoneobj.API_SetLine(this.line);
    }

    private void sipDisconect() {
        webphoneobj.API_Unregister();
        webphoneobj.API_Stop();
        webphoneobj.API_Exit();
        webphoneobj = null;
    }

}
