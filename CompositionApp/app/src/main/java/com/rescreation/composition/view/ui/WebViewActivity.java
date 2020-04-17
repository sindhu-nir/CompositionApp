package com.rescreation.composition.view.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.rescreation.composition.R;
import com.rescreation.composition.model.repository.RetrofitClient;
import com.rescreation.composition.view.adapter.myWebViewClient;

public class WebViewActivity extends AppCompatActivity {

    WebView webView;
    ProgressBar bar;

    Bundle bundle;
    String webUrl="",product_name="",catName="";
    ImageButton backImageButton;
    TextView titleTextView;
    Context mContext;
    public String BASE_URL = "";

    private LottieAnimationView emptyAnimation;

    private AdView mAdView;

    private InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        backImageButton=findViewById(R.id.backImageButton);
        titleTextView=findViewById(R.id.title);
        backImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                    finish();
                }
            }
        });

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

       // BASE_URL=RetrofitClient.BASE_URL;
        BASE_URL=pref.getString("baseURL", null);         // getting String
        emptyAnimation = findViewById(R.id.emptyAnimation);
        mContext = WebViewActivity.this;
        webView=(WebView) findViewById(R.id.webView);
        bar = (ProgressBar) findViewById(R.id.progress1);
        bundle = getIntent().getExtras();
        product_name = bundle.getString("product_name");
        catName = bundle.getString("catName");
        webUrl=BASE_URL+"files/"+catName+"/"+product_name+".php";
        titleTextView.setText(catName.toUpperCase());
        LoadWebView();

        // initialize the AdMob app
        MobileAds.initialize(this, getString(R.string.admob_app_id));
        mAdView = findViewById(R.id.adView);
        LoadAdmobAds();
        initializeInterstitialAds();
    }

    private void LoadWebView() {

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
 //       webSettings.setUseWideViewPort(true);
//        webSettings.setLoadWithOverviewMode(true);
        webSettings.setBuiltInZoomControls(true);
//        webSettings.setGeolocationEnabled(true);
//        webSettings.setDomStorageEnabled(true);
//        webSettings.setDatabaseEnabled(true);
//        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
//        webSettings.setAllowFileAccess(true);
//        webSettings.setAppCacheEnabled(true);
        webSettings.setDisplayZoomControls(false);
        if (Build.VERSION.SDK_INT >= 21) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webView.setWebViewClient(new myWebViewClient(bar));
        webView.loadUrl(webUrl);


        webView.setDownloadListener((url, userAgent, contentDisposition, mimetype, contentLength) -> {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });



    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && this.webView.canGoBack()) {
            this.webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
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
                finish();
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

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdView != null) {
            mAdView.destroy();
        }


    }


    private void initializeInterstitialAds() {
        // Initialize the Mobile Ads SDK.
      //  MobileAds.initialize(mContext, getString(R.string.admob_app_id));

        // Create the InterstitialAd and set the adUnitId.
        mInterstitialAd = new InterstitialAd(this);
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
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
            finish();
        }
    }
}
