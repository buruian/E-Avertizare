/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iucosoft.eavertizare.util;

import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.MessageFactory;
import com.twilio.sdk.resource.instance.Message;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

/**
 *
 * @author valera_
 */
public class MySmsSender {

    // Find your Account Sid and Token at twilio.com/user/account 
//    public static final String ACCOUNT_SID = "ACd1d57f2058cab7f1f9e4af4e312bf18c";
//    public static final String AUTH_TOKEN = "1801eaf638858bff4832d9912db41ccb";
    public static final String ACCOUNT_SID = "ACe668b8b2a2d9cbad57ae12d4ec789631";
    public static final String AUTH_TOKEN = "ad76e3b9dd743d5dee7553114ce20dc5";

    public void sendSms(int nrTelefon, String msg){
        TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN);

        // Build the parameters 
        List<NameValuePair> params = new ArrayList<NameValuePair>();
     //   params.add(new BasicNameValuePair("To", "+37369287969"));
        params.add(new BasicNameValuePair("To", "+373" + nrTelefon));
     //   params.add(new BasicNameValuePair("From", "+12017316900"));
        params.add(new BasicNameValuePair("From", "+14155992671"));
        params.add(new BasicNameValuePair("Body", msg));

        MessageFactory messageFactory = client.getAccount().getMessageFactory();
       try {
            Message message = messageFactory.create(params);
            // System.out.println(message.getSid()); 
        } catch (TwilioRestException ex) {
            Logger.getLogger(MySmsSender.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
}
