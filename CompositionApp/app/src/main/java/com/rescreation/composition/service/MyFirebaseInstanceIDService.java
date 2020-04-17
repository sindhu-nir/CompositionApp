package com.rescreation.composition.service;

/**
 * Created by root on 8/16/18.
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.rescreation.composition.model.Config;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;


public class MyFirebaseInstanceIDService extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();

   // private static final String TAG = "MyFirebaseIdService";
    private static final String TOPIC_GLOBAL = "global";


    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        FirebaseApp.initializeApp(this);
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        final String[] newToken = {""};

        // now subscribe to `global` topic to receive app wide notifications
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC_GLOBAL);


        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                newToken[0] = instanceIdResult.getToken();
            }
        });

        // Saving reg id to shared preferences
        storeRegIdInPref(newToken[0]);

        // sending reg id to your server
        sendRegistrationToServer(refreshedToken);

        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void sendRegistrationToServer(final String token) {
        // sending gcm token to server
        Log.e(TAG, "sendRegistrationToServer: " + token);
    }

    private void storeRegIdInPref(String token) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("regId", token);
        editor.putString("topic", TOPIC_GLOBAL);
        editor.commit();
    }
}