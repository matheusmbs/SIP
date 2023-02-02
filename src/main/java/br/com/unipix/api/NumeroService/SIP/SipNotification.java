package br.com.unipix.api.NumeroService.SIP;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.unipix.api.NumeroService.model.Numero;
import br.com.unipix.api.NumeroService.service.NumeroService;
import webphone.webphone;

public class SipNotification extends Thread {
    boolean terminated = false;
    webphone webphoneobj = null;
    String arquivoCDR;
    
    @Autowired
    NumeroService numeroService;


    public SipNotification(webphone webphoneobj_in) throws IOException {
        webphoneobj = webphoneobj_in;
    }

    // start the thread

    public boolean Start() {
        try {

            this.start();

            System.out.println("sip notifications started");

            return true;

        } catch (Exception e) {
            System.out.println("Exception at SIPNotifications Start: " + e.getMessage() + "\r\n" + e.getStackTrace());
        }
        return false;
    }

    // stop the thread

    public void Stop()

    {
        terminated = true;
    }

    // blocking read in this thread

    public void run() {
        try {
            String sipnotifications = "";
            String[] notarray = null;
            while (!terminated) {
                sipnotifications = webphoneobj.API_GetNotificationsSync();
                if (sipnotifications != null && sipnotifications.length() > 0) {
                    notarray = sipnotifications.split("\r\n");
                    if (notarray == null || notarray.length < 1) {
                        if (!terminated)
                            Thread.sleep(1); // some error occured. sleep a bit just to be sure to avoid busy loop
                    } else {
                        for (int i = 0; i < notarray.length; i++) {
                            if (notarray[i] != null && notarray[i].length() > 0) {
                                ProcessNotifications(notarray[i]);
                            }
                        }
                    }
                }

                else {
                    if (!terminated)
                        Thread.sleep(1); // some error occured. sleep a bit just to be sure to avoid busy loop
                }
            }
        } catch (Exception e) {
            if (!terminated)
                System.out.println("Exception at SIPNotifications run: " + e.getMessage() + "\r\n" + e.getStackTrace());
        }

    }

    public void ProcessNotifications(String msg) {

        try {
            msg = msg.replace("WPNOTIFICATION,", "");
            String[] msgr = msg.split(",");

            // if (msgr[0].equals("STATUS")) {
            //     this.notifications.add(msg);
            // }

            if (msgr[0].equals("CDR")) {
                // Aqui eu pego o CDR da ligação
                Numero numero = new Numero();
                LocalDateTime today = LocalDateTime.now();
                numero.setDataFinalizacao(today);
                numero.setStatusProcessamento("Processado");
            }

        } catch (Exception e) {
            System.out.println("Exception at SIPNotifications ProcessNotifications: " + e.getMessage() + "\r\n"
                    + e.getStackTrace());
        }
    }

}
