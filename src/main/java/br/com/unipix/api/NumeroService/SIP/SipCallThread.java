package br.com.unipix.api.NumeroService.SIP;

import java.util.concurrent.TimeUnit;
import br.com.unipix.api.NumeroService.model.Numero;
import java.io.IOException;
import webphone.webphone;

public class SipCallThread implements Runnable {
    webphone webphoneobj;
    Integer line;
    Numero numero;

    public SipCallThread(webphone webphoneobj, Numero numero, Integer line)
            throws IOException {
        this.webphoneobj = webphoneobj;
        this.numero = numero;
        this.line = line;
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        this.webphoneobj.API_Call(this.line, this.numero.getNumero());

        // Loop para pegar os detalhes da ligação
        while (true) {
            try {
                long endTime = System.currentTimeMillis();
                long elapsedTime = endTime - startTime;
                long seconds = TimeUnit.MILLISECONDS.toSeconds(elapsedTime);

                if (seconds > 20) {
                    break;
                }

                String getDetail = webphoneobj.API_GetLineDetails(this.line);
                if (getDetail.contains(this.numero.getNumero()) && getDetail.contains("Finished")) {
                    break;
                }
            } catch (Exception e) {

            }

        }
    }

}
