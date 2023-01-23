package br.com.unipix.api.NumeroService.SIP;

import java.time.LocalDateTime;
import br.com.unipix.api.NumeroService.model.Numero;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.math.NumberUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import webphone.webphone;


public class SipCallThread implements Runnable {
    webphone webphoneobj = new webphone();
    Integer line;
    Numero numero;

    public SipCallThread(Numero numero, Integer line) throws IOException {
        this.numero = numero;
        this.line = line;
        // Nessa função, é realizada simultaneamente a conexão de cada instância de registro e a realização da ligação. É importante destacar que é possível realizar essas tarefas de forma separada utilizando os métodos disponíveis do mizu-voip.
        this.sipConect();
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();

        //Loop para pegar os detalhes da ligação
        while (true) {
            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;
            long seconds = TimeUnit.MILLISECONDS.toSeconds(elapsedTime);

            //Em caso de não haver a exibição do status 'Finished' após 15 segundos da realização da chamada, o loop será encerrado. É importante destacar que essa condição é rara de ocorrer quando é utilizada a prática de criar uma instância para cada ligação.
            if (seconds > 15) {
                LocalDateTime today = LocalDateTime.now();
                numero.setDataProcessamento(today);
                numero.setStatusCode(1000);
                break;
            }

            //Função para obtenção dos detalhes da ligação.
            String statusCall = this.webphoneobj.API_GetLineDetails(this.line);

            //Foi necessário incluir essa condição de verificação, pois mesmo solicitando o status do canal específico dessa classe, houve a ocorrência de retorno de informações referentes a outro canal. Dessa forma, é realizada a verificação se o número do detalhe da ligação é o mesmo da classe em questão.
            if (statusCall.contains(this.numero.getNumero())) {

                //"Caso o status da ligação seja 'Finished', o loop será encerrado e serão obtidos os detalhes do protocolo SIP."
                if (statusCall.contains("Finished")) {
                    String sipMessagem = webphoneobj.API_GetSIPMessage(this.line, 0, 2);
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
                    this.sipDisconect();
                    break;
                }
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

        // Alguns dos parâmetros utilizados nesta função foram passados somente como teste, e podem ser ajustados ou removidos conforme necessário.
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
        webphoneobj.API_SetLine(this.line);
    }

    private void sipDisconect() {
        webphoneobj.API_Unregister();
        webphoneobj.API_Stop();
        webphoneobj.API_Exit();
        webphoneobj = null;
    }

}
