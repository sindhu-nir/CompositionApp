package com.rescreation.composition.view.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rescreation.composition.R;
import com.rescreation.composition.model.repository.RetrofitClient;
import com.rescreation.composition.model.repository.RetrofitService;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class SplashScreenActivity extends AppCompatActivity {


    private LottieAnimationView openBookAnimation;
    Context mContext;
    Handler handler;
    ProgressBar bar;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    DatabaseReference myFirebaseRef;
    FirebaseDatabase database;
    private  String url="";
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);


        mContext=SplashScreenActivity.this;
        openBookAnimation = findViewById(R.id.openBookAnimation);

        openBookAnimation.setVisibility(View.VISIBLE);
        bar = (ProgressBar) findViewById(R.id.progress1);

        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();

        database = FirebaseDatabase.getInstance();

        if (isNetworkConnected()) {
            if (!isInternetAvailable()){
                ShowAlert("Failed", "Internet Connection Not Working Properly , Check your mobile Data or WIFI");
                return;
            }

           // splash();
            url=GetFirebaseValue("base_url");

        } else {
            ShowAlert("No Internet Connection", "Connect to the internet \n and try again");
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }

    private void splash() {
        new Thread(new Runnable() {
            public void run() {
                doWork();
                startApp();
                finish();
            }
        }).start();
    }

    private void doWork() {
        for (int progress = 0; progress < 100; progress += 10) {
            try {
                Thread.sleep(300);
                bar.setProgress(progress);
            } catch (Exception e) {
                e.printStackTrace();
                //Timber.e(e.getMessage());
            }
        }
    }

    private void startApp() {

        if (isNetworkConnected()) {

//            Intent intent = new Intent(SplashScreen.this, MainActivity.class);
            Intent intent = new Intent(mContext, MainActivity.class);
            startActivity(intent);
            finish();

        } else {
            ShowAlert("ERROR","No Internet Connection");
            Toast.makeText(mContext, "No Internet Connection", Toast.LENGTH_LONG).show();

        }


    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(mContext.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public void ShowAlert(String title, String msg) {

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


    public  String GetFirebaseValue(String firebase_parameter) {
        final String[] firebase_Value = {""};
        myFirebaseRef = database.getReference(firebase_parameter);

        final String url_1 = "";
        myFirebaseRef.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                firebase_Value[0] = dataSnapshot.getValue(String.class).toString();
                url = firebase_Value[0];
                // toastMessage(firebase_Value[0]+" From Inside OnDataChange");

                editor.putString("baseURL", url);
                editor.apply();

                startApp();

            }

            public void onCancelled(DatabaseError error) {
                System.out.println("Firebase Error: "+error.getMessage());
            }
        });

        //toastMessage(firebase_Value[0]);
        return url_1;
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
