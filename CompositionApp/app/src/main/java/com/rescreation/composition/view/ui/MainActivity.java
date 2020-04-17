package com.rescreation.composition.view.ui;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.glide.slider.library.animations.DescriptionAnimation;
import com.glide.slider.library.slidertypes.BaseSliderView;
import com.glide.slider.library.slidertypes.TextSliderView;
import com.glide.slider.library.tricks.ViewPagerEx;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.common.util.Strings;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.rescreation.composition.R;
import com.rescreation.composition.model.Category;
import com.rescreation.composition.model.CategoryResponse;
import com.rescreation.composition.model.Config;
import com.rescreation.composition.model.FkeyEntryResponse;
import com.rescreation.composition.model.repository.RetrofitClient;
import com.rescreation.composition.model.repository.RetrofitService;
import com.rescreation.composition.util.NotificationUtils;
import com.rescreation.composition.view.adapter.MainActivityAdapter;
import com.rescreation.composition.viewmodel.MainActivityViewModel;

import java.util.ArrayList;
import java.util.List;
import com.bumptech.glide.request.RequestOptions;
import com.glide.slider.library.SliderLayout;


import java.util.ArrayList;
public class MainActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener,
        ViewPagerEx.OnPageChangeListener{

    MainActivityViewModel mainActivityViewModel;

    ArrayList<Category> categoryArrayList;
    MainActivityAdapter categoryAdapter;
    RecyclerView categoryRecycleView;
    ProgressBar progressBar;


    //Admob
    private AdView mAdView;
    //Firebase Messaging
    //Firebase Noti
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    Context mContext;

    DatabaseReference myFirebaseRef;
    FirebaseDatabase database;
    private  String url="";
    RetrofitService retrofitService;

    SharedPreferences pref;
    SharedPreferences pref_firebase;
    SharedPreferences.Editor editor;
    SharedPreferences.Editor fkeyEditor;
    public String BASE_URL = "",newToken="",fkey="";
    Button feedbackButton;

    ImageButton backImageButton;
    TextView titleTextView;
    private SliderLayout sliderShow;
    private SliderLayout mDemoSlider;

    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = MainActivity.this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        backImageButton=findViewById(R.id.backImageButton);
        titleTextView=findViewById(R.id.title);
        backImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowExitDialog();
            }
        });

        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        pref_firebase = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);

        editor = pref.edit();
        fkeyEditor = pref_firebase.edit();

        database = FirebaseDatabase.getInstance();
      //  url = GetFirebaseValue("base_url");

        categoryRecycleView = findViewById(R.id.menuRecycleView);
        progressBar = findViewById(R.id.progressBar);
        feedbackButton = findViewById(R.id.feedbackButton);
        //sliderShow = findViewById(R.id.slider);
        mDemoSlider = findViewById(R.id.slider);
        progressBar.setVisibility(View.VISIBLE);

        categoryArrayList = new ArrayList<>();

        BASE_URL=pref.getString("baseURL", null);
        fkey=pref_firebase.getString("fkey", null);

        retrofitService = RetrofitClient.getClient(BASE_URL).create(RetrofitService.class);


        //Admob
        // initialize the AdMob app
        MobileAds.initialize(this, getString(R.string.admob_app_id));
        mAdView = findViewById(R.id.adView);
        LoadAdmobAds();

        //Firebase
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    // FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");
                    String title = intent.getStringExtra("title");

                    // Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                    //txtMessage.setText(message);
                    SendNodification(message,title);

                }
            }
        };

        displayFirebaseRegId();
        inflateImageSlider();
        initializeInterstitialAds();
        SetUpViewModel();
        feedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent productIntent=new Intent(mContext, FeedbackActivity.class);
                startActivity(productIntent);
            }
        });

    }

    private void initializeInterstitialAds() {
        // Initialize the Mobile Ads SDK.
       // MobileAds.initialize(mContext, getString(R.string.admob_app_id));

        // Create the InterstitialAd and set the adUnitId.
        mInterstitialAd = new InterstitialAd(mContext);
        // Defined in res/values/strings.xml
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_id));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the interstitial ad is closed.
                System.exit(1);
            }
        });

    }

    private void setupRecyclerView() {
        if (categoryAdapter == null) {
            categoryAdapter = new MainActivityAdapter(MainActivity.this, categoryArrayList);
            //categoryRecycleView.setLayoutManager(new LinearLayoutManager(this));
            categoryRecycleView.setLayoutManager(new GridLayoutManager(this,2));
            categoryRecycleView.setAdapter(categoryAdapter);
            categoryRecycleView.setItemAnimator(new DefaultItemAnimator());
            categoryRecycleView.setNestedScrollingEnabled(true);
        } else {
            categoryAdapter.notifyDataSetChanged();
        }
    }


    private void LoadAdmobAds() {
        mAdView = (AdView) findViewById(R.id.adView);

        try {
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
            }

            @Override
            public void onAdClosed() {
                //  Toast.makeText(getApplicationContext(), "Ad is closed!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                //   Toast.makeText(getApplicationContext(), "Ad failed to load! error code: " + errorCode, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLeftApplication() {
                // Toast.makeText(getApplicationContext(), "Ad left application!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }
        });


    }

    @Override
    public void onPause() {
        super.onPause();
        if (mAdView != null) {
            mAdView.pause();
        }
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);


    }

    @Override
    public void onResume() {
        super.onResume();
      //  sliderShow.startAutoCycle();

        if (mAdView != null) {
            mAdView.resume();
        }
        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdView != null) {
            mAdView.destroy();
        }
        editor.clear();
        editor.apply();

    }

    private void displayFirebaseRegId() {
        String regId = pref_firebase.getString("newToken", null);


        //Log.e(TAG, "Firebase reg id: " + regId);
        FirebaseMessaging.getInstance().subscribeToTopic("global")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Topic Subscribed";
                        if (!task.isSuccessful()) {
                            msg = "Subcription to Topic Failed";
                          //  Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                        //Log.d(TAG, msg);
                        //Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
//        if (!TextUtils.isEmpty(regId)){}
//        //txtRegId.setText("Firebase Reg Id: " + regId);
//        //Toast.makeText(getApplicationContext(), "Firebase Reg Id: " + regId, Toast.LENGTH_LONG).show();
//        else
//            //txtRegId.setText("Firebase Reg Id is not received yet!");
//            Toast.makeText(getApplicationContext(), "Firebase Reg Id is not received yet! " + regId, Toast.LENGTH_LONG).show();

    }
    //Firebase Noti
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void SendNodification(String message, String title) {
        Intent notificationIntent = new Intent(MainActivity.this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(MainActivity.this,
                10, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationManager nm = (NotificationManager) MainActivity.this
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Resources res = MainActivity.this.getResources();
        Notification.Builder builder = new Notification.Builder(MainActivity.this);

        builder.setContentIntent(contentIntent)
                // .setSmallIcon(Icon.createWithBitmap(BitmapFactory.decodeResource(res, R.drawable.bell_icon)))
                .setSmallIcon(R.drawable.star)
                .setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.ic_launcher))
                .setTicker(res.getString(R.string.app_name))
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(message);
        Notification n = builder.build();

        nm.notify(10, n);
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

                retrofitService = RetrofitClient.getClient(url).create(RetrofitService.class);
                SetUpViewModel();

            }

            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });

        //toastMessage(firebase_Value[0]);
        return url_1;
    }

    public void SetUpViewModel(){
        mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        mainActivityViewModel.init();

        progressBar.setVisibility(View.VISIBLE);

        setupRecyclerView();

        mainActivityViewModel.getCategoryRepository().observe(this, categoryResponse -> {
            if (categoryResponse!=null) {
                progressBar.setVisibility(View.GONE);
                List<Category> categoryList = categoryResponse.getCategories();
                categoryArrayList.addAll(categoryList);
                categoryAdapter.notifyDataSetChanged();

                newToken=pref_firebase.getString("newToken", null);
                if(!Strings.isEmptyOrWhitespace(newToken)) {
                    if (!newToken.isEmpty() && !newToken.equals(fkey) && newToken != null) {
                        CheckFkeyToServer(newToken);
                    }
                }

            }
            else{
                progressBar.setVisibility(View.GONE);
                Toast.makeText(this, "Nothing Found", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void CheckFkeyToServer(String newToken) {

        Call<FkeyEntryResponse> call3 =  retrofitService.CheckFirebaseToken(newToken);
        call3.enqueue(new Callback<FkeyEntryResponse>() {
            @Override
            public void onResponse(Call<FkeyEntryResponse> call, Response<FkeyEntryResponse> response) {
                int count=0;
                if (response.isSuccessful()) {
                    FkeyEntryResponse loginResponse = response.body();
                    String message = loginResponse.getMessage();
                    String status = loginResponse.getStatus();
                    if (status.equals("1")){
                        count=1;

                        fkeyEditor.putString("fkey", newToken);
                        fkeyEditor.apply();
                    }
                    else{
                      //  mMaster.ShowAlert(mContext, "Failed", message);
                        count=-1;
                    }


                }else{
                    FkeyEntryResponse loginResponse = response.body();
                    String message = loginResponse.getMessage();
                    String status = loginResponse.getStatus();

                }


            }

            @Override
            public void onFailure(Call<FkeyEntryResponse> call, Throwable t) {
                call.cancel();

            }
        });
    }

    @Override
    public void onBackPressed() {
        ShowExitDialog();
    }

    public void ShowExitDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (mInterstitialAd.isLoaded()) {
                            mInterstitialAd.show();
                        } else {
                            Log.d("TAG", "The interstitial wasn't loaded yet.");
                            finish();
                        }


                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mDemoSlider.stopAutoCycle();

    }

    private void inflateImageSlider() {


        ArrayList<String> listUrl = new ArrayList<>();
        ArrayList<String> listName = new ArrayList<>();

        listUrl.add(BASE_URL+"images/banner1.png");
        listName.add("English Composition");

        listUrl.add(BASE_URL+"images/banner2.png");
        listName.add("English Composition");

        listUrl.add(BASE_URL+"images/banner3.png");
        listName.add("English Composition");

        listUrl.add(BASE_URL+"images/banner4.png");
        listName.add("English Composition");

        listUrl.add(BASE_URL+"images/banner0.png");
        listName.add("RES Creations");

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.centerCrop()
        .diskCacheStrategy(DiskCacheStrategy.NONE);
//        .placeholder(R.drawable.banner0)
//        .error(R.drawable.banner0);

        for (int i = 0; i < listUrl.size(); i++) {
            TextSliderView sliderView = new TextSliderView(this);
            // if you want show image only / without description text use DefaultSliderView instead

            // initialize SliderLayout
            sliderView
                    .image(listUrl.get(i))
                    .description(listName.get(i))
                    .setRequestOption(requestOptions)
                    .setProgressBarVisible(true)
                    .setOnSliderClickListener(this);


            //add your extra information
            sliderView.bundle(new Bundle());
            sliderView.getBundle().putString("extra", listName.get(i));
            mDemoSlider.addSlider(sliderView);
        }

        // set Slider Transition Animation
        // mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);

        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);

    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
    //    Toast.makeText(this, slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
