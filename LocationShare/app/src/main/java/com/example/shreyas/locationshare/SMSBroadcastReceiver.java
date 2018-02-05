package com.example.shreyas.locationshare;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * Created by shreyas on 5/2/18.
 */

public class SMSBroadcastReceiver extends BroadcastReceiver {

    private DBHandler dbHandler;
    private LocationManager locationManager;
    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;

        if (intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
            StringBuilder messageReceived = new StringBuilder();
            SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
            String messageSender = messages[0].getDisplayOriginatingAddress();
            dbHandler = new DBHandler(context);
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

            try {
                for (int i = 0; i < messages.length; i++) {
                    messageReceived.append(messages[i].getMessageBody());
                }
                processMessage(messageReceived.toString(), messageSender);
                dbHandler.close();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    private void processMessage(String message, String messageSender) {

        // Format messageSender to retrieve Number from the TrustedDB
        Contact contact = dbHandler.getContactById(ContactFormat.format(messageSender));

        if (contact != null && message.toLowerCase().contains("location")) {
            Log.e("TAG", "All checks passed!");
            Location location = null;
            if (PermissionManager.checkPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)) {
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                sendLocationMessage(location, messageSender);
            }
        } else {
            Log.e("TAG", "Not a trusted contact or Keyword not found!");
        }
    }

    private void sendLocationMessage(Location location, String messageSender) {
        if (PermissionManager.checkPermission(context, Manifest.permission.SEND_SMS) &&
                PermissionManager.checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
            SmsManager smsManager = SmsManager.getDefault();
            String GPSCoordinatedToBeSent = "Current Coordinates is \nLatitude: " + location.getLatitude() + "\n" +
                    "Longitude: " + location.getLongitude();
            smsManager.sendTextMessage(messageSender, null, GPSCoordinatedToBeSent, null, null);
        }
    }
}
