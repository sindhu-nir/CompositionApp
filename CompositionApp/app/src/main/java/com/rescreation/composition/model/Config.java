package com.rescreation.composition.model;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.os.StrictMode;

import com.rescreation.composition.R;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import androidx.appcompat.app.AlertDialog;

public class Config {


    // global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "global";

    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";

    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

    public static final String SHARED_PREF = "ah_firebase";


    public void ShowAlert(Context mContext, String title, String msg) {

        AlertDialog.Builder dlgAlert=new AlertDialog.Builder(mContext);

        //android.support.v7.app.AlertDialog.Builder dlgAlert = new android.support.v7.app.AlertDialog.Builder(mContext);

        dlgAlert.setMessage(msg);
        dlgAlert.setTitle(title);
        dlgAlert.setIcon(R.mipmap.ic_launcher);
        dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // call your code here
                System.exit(1);
            }
        });
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }

    public boolean isInternetAvailable() {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        boolean connection=false;
        try {

            int timeoutMs = 1500;
            Socket sock = new Socket();
            InetSocketAddress sockaddr = new InetSocketAddress("8.8.8.8", 53);

            sock.connect(sockaddr, 1500);
            sock.close();

            connection=true;
        } catch (IOException io) {
            connection=false;

        }
        return connection;
    }
}
