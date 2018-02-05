package com.example.shreyas.locationshare;

import android.util.Log;

/**
 * Created by shreyas on 5/2/18.
 */

public class ContactFormat {

    public static String format(String contact) {
        if (contact.length() >= 10) {
            if (contact.substring(0, 3).equals("+91")) {
                contact =  contact.substring(3,contact.length()).trim();
                contact = contact.replace(" ","");
                return contact;
            } else {
                return contact.replace(" ","");
            }
        }
        return "Error";
    }

}
