package br.com.unipix.api.NumeroService.SIP;

import java.util.ArrayList;
import java.util.List;

import webphone.webphone;

public class SipNotification extends Thread {
    boolean terminated = false;
    webphone webphoneobj = null;
    List<String> notifications = new ArrayList();
  

   public SipNotification(webphone webphoneobj_in){
       webphoneobj = webphoneobj_in;
   }

 

   //start the thread

   public boolean Start(){
       try{

           this.start();

           System.out.println("sip notifications started");

           return true;

       }catch(Exception e) {System.out.println("Exception at SIPNotifications Start: "+e.getMessage()+"\r\n"+e.getStackTrace()); }
       return false;
   }

 

  //stop the thread

   public void Stop()

   {
        terminated = true;
        this.notifications.clear();
   }

 

   //blocking read in this thread

   public void run(){
        try{
           String sipnotifications = "";
           String[] notarray = null;
           while (!terminated){
                  sipnotifications = webphoneobj.API_GetNotificationsSync();
                  if (sipnotifications != null && sipnotifications.length() > 0){
                      notarray = sipnotifications.split("\r\n");
                      if (notarray == null || notarray.length < 1){                               
                         if(!terminated) Thread.sleep(1); //some error occured. sleep a bit just to be sure to avoid busy loop
                      }
                      else{
                        for (int i = 0; i < notarray.length; i++){
                            if (notarray[i] != null && notarray[i].length() > 0){                                        
                                ProcessNotifications(notarray[i]);
                            }
                        }
                      }
                  }

                  else{
                      if(!terminated) Thread.sleep(1);  //some error occured. sleep a bit just to be sure to avoid busy loop
                  }                                       
           }
        }catch(Exception e){
           if(!terminated) System.out.println("Exception at SIPNotifications run: "+e.getMessage()+"\r\n"+e.getStackTrace());
        }

    }

    public List<String> getNotifications() {
        return notifications;
    }
 

    public void ProcessNotifications(String msg){

        try{  
            msg = msg.replace("WPNOTIFICATION,", "");
            String[] arrMsg = msg.split(",");
            String statusNotify = arrMsg[0];
            if(statusNotify.equals("STATUS")){
                String statusCall = arrMsg[2];
                if(statusCall.equals("CallSetup")){
                    this.notifications.add(msg);
                }
            }
        }catch(Exception e) { System.out.println("Exception at SIPNotifications ProcessNotifications: "+e.getMessage()+"\r\n"+e.getStackTrace()); }
    }
}

 

