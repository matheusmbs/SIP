package br.com.unipix.api.NumeroService.SIP;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.math.NumberUtils;

import br.com.unipix.api.NumeroService.model.Numero;
import java.io.IOException;

import webphone.webphone;

public class SipCallThread implements Runnable {
    webphone webphoneobj;
    Integer line;
    Numero numero;

    List<String> notification = new ArrayList<>();

    public SipCallThread(webphone webphoneobj, Numero numero, Integer line) throws IOException {
        this.webphoneobj = webphoneobj;
        this.numero = numero;
        this.line = line;
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        this.webphoneobj.API_Call(this.line, this.numero.getNumero());

        String notificationAux = "";

        // Loop para pegar os detalhes da ligação
        while (true) {

            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;
            long seconds = TimeUnit.MILLISECONDS.toSeconds(elapsedTime);
            if (seconds > 20) {
                String sipMessagem = webphoneobj.API_GetSIPMessage(this.line, 0, 2);
                this.notification.add(sipMessagem);
                break;
            }

            System.out.println(this.webphoneobj.API_GetLineDetails(this.line));
            System.out.println(webphoneobj.API_GetSIPMessage(this.line, 0, 2));
            System.out.println(webphoneobj.API_GetSIPMessage(this.line, 0, 1));

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            String statusCall = this.webphoneobj.API_GetLineDetails(this.line);

            if(!statusCall.equals(notificationAux)){
                this.notification.add(statusCall);
                notificationAux = statusCall;
            }

            // // Em caso de não haver a exibição do status 'Finished' após 15 segundos da
            // // realização da chamada, o loop será encerrado. É importante destacar que
            // essa
            // // condição é rara de ocorrer quando é utilizada a prática de criar uma
            // // instância para cada ligação.
            // if (seconds > 20) {
            // this.webphoneobj.API_Hangup(this.line);
            // }

            // if (seconds > 25) {
            // LocalDateTime today = LocalDateTime.now();
            // numero.setDataProcessamento(today);
            // numero.setStatusCode(1000);
            // break;
            // }

            // // Função para obtenção dos detalhes da ligação.
            // String statusCall = this.webphoneobj.API_GetLineDetails(this.line);

            // // Foi necessário incluir essa condição de verificação, pois mesmo
            // solicitando o
            // // status do canal específico dessa classe, houve a ocorrência de retorno de
            // // informações referentes a outro canal. Dessa forma, é realizada a
            // verificação
            // // se o número do detalhe da ligação é o mesmo da classe em questão.
            // if (statusCall.contains(this.numero.getNumero())) {

            // if (statusCall.contains("Speaking")) {
            // this.webphoneobj.API_Hangup(this.line);
            // }

            // // "Caso o status da ligação seja 'Finished', o loop será encerrado e serão
            // // obtidos os detalhes do protocolo SIP."
            // if (statusCall.contains("Finished")) {
            // String sipMessagem = webphoneobj.API_GetSIPMessage(this.line, 0, 2);
            // if (sipMessagem.contains("To: <sip:" + this.numero.getNumero())) {
            // String[] sipMensagemLines = sipMessagem.split("\n");
            // String statusLine = sipMensagemLines[0];
            // String statusCode = statusLine.split(" ")[1];
            // String callId = sipMensagemLines[4].replace("Call-ID: ", "").replace("\r",
            // "");
            // LocalDateTime today = LocalDateTime.now();
            // numero.setDataProcessamento(today);
            // if (NumberUtils.isDigits(statusCode)) {
            // numero.setStatusCode(Integer.parseInt(statusCode));
            // }
            // numero.setCallId(callId);
            // }
            // break;
            // }
            // }
        }
    }

    public Numero getNumero() {
        return this.numero;
    }

    public List<String> getNotification() {
        return this.notification;
    }
}
