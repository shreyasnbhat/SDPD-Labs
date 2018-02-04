package com.example.shreyas.trustedcontact;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by shreyas on 5/2/18.
 */

public class SMSBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
            Bundle bundle = intent.getExtras();
            StringBuilder messageReceived = new StringBuilder();
            SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
            try {
                for (int i = 0; i < messages.length; i++) {
                    messageReceived.append(messages[i].getMessageBody());
                }
                Log.e("TAG",messageReceived.toString());
            } catch (NullPointerException e){
                e.printStackTrace();
            }
        }
    }
}
