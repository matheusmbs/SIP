package br.com.unipix.api.NumeroService.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.util.StringUtil;
import org.springframework.stereotype.Service;
import br.com.unipix.api.NumeroService.SIP.SipNotification;
import br.com.unipix.api.NumeroService.model.Numero;
import webphone.webphone;

@Service
public class SipService {
    private webphone wobj;
    private SipNotification sipNotification = null;
    private Integer line = 1;

    public void ligar(List<Numero> numeros) throws IOException,
            InterruptedException, ExecutionException {
        if (wobj == null) {
            this.sipStart();
        }

        Thread.sleep(3000);

        for (Numero n : numeros) {
            Thread.sleep(20);
            n.setLine(this.line);
            this.wobj.API_Call(this.line, n.getNumero());
            this.line++;
        }
    }

    public List<Numero> processarNumero(List<Numero> numeros, List<String> cdrs, webphone wobj) throws IOException {
        String text = "";
        for (String cdr : cdrs) {
            String[] cdrArr = cdr.split(",");
            Integer iN = numeros.stream()
                    .filter(n -> n.getLine().toString().equals(cdrArr[1])).map(numeros::indexOf)
                    .findFirst()
                    .orElse(-1);

            if (iN != -1) {
                numeros.get(iN).setNumero(cdrArr[2]);
                numeros.get(iN).setCallId(cdrArr[10].replace("[", "").replace("]", ""));
                LocalDateTime today = LocalDateTime.now();
                numeros.get(iN).setDataProcessamento(today);
                numeros.get(iN).setStatusPlataforma(cdrArr[9]);

                if (cdrArr[9].equals("bye Maximum call time exceeded")) {
                    numeros.get(iN).setStatusMessage("Ok");
                    numeros.get(iN).setStatusCode(200);
                } else if (cdrArr[9].equals("call setup timeout (no connect response)")) {
                    numeros.get(iN).setStatusMessage("");
                    numeros.get(iN).setStatusCode(-1);
                } else {
                    String sipMessage = wobj.API_GetLastRecSIPMessage(cdrArr[1]);
                    sipMessage = sipMessage.replace("\r", "");
                    if (sipMessage.contains(cdrArr[2])) {
                        String[] textArr = sipMessage.split("\n");
                        String[] statusMssArr = textArr[0].split(" ");
                        String statusMessage = "";
                        for (int i = 2; i < statusMssArr.length; i++) {
                            statusMessage += statusMssArr[i] + " ";
                        }
                        String statusCode = textArr[0].split(" ")[1];
                        numeros.get(iN).setStatusMessage(statusMessage);
                        if (NumberUtils.isDigits(statusCode)) {
                            numeros.get(iN).setStatusCode(Integer.parseInt(statusCode));
                        } else {
                            numeros.get(iN).setStatusCode(-1);
                        }

                    } else {
                        numeros.get(iN).setStatusCode(-1);
                    }
                }
                String[] arr = { numeros.get(iN).getNumero(), numeros.get(iN).getStatusCode().toString(),
                        numeros.get(iN).getStatusMessage(),
                        numeros.get(iN).getStatusPlataforma(), numeros.get(iN).getCallId() };
                text += StringUtil.join(arr, ",").replace("\n", "") + "\n";
            }
        }

        FileWriter fileWriter = new FileWriter("numeros.txt", true);
        BufferedWriter writer = new BufferedWriter(fileWriter);
        writer.write(text);
        writer.close();
        fileWriter.close();
        return numeros;
    }

    private void sipStart() throws IOException {
        URL whatismyip = new URL("http://checkip.amazonaws.com");
        BufferedReader in = new BufferedReader(new InputStreamReader(
                whatismyip.openStream()));
        String ip = in.readLine();
        wobj = new webphone();
        wobj.API_Start();
        wobj.API_SetParameter("serveraddress", "177.53.17.120:5060");
        wobj.API_SetParameter("username", "55111185744186");
        wobj.API_SetParameter("password", "W5UF6SpQajf6zR");
        wobj.API_SetParameter("proxyaddress", ip);
        wobj.API_SetParameter("register", "1");
        wobj.API_SetParameter("maxsimcalls", "1000");
        wobj.API_SetParameter("maxlinesex", "1000");
        wobj.API_Start();
        this.sipNotification = new SipNotification(wobj);
        this.sipNotification.start();
    }

    private void sipDisconect() {
        wobj.API_Stop();
        wobj.stop();
        wobj.API_Exit();
        wobj = null;
        this.sipNotification = null;
    }
}
