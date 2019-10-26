package com.rescreation.composition.view.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rescreation.composition.R;
import com.rescreation.composition.model.repository.RetrofitClient;
import com.rescreation.composition.view.adapter.myWebViewClient;

public class WebViewActivity extends AppCompatActivity {

    WebView webView;
    ProgressBar bar;

    Bundle bundle;
    String webUrl="",product_name="",modelName;
    ImageButton backImageButton;
    TextView titleTextView;
    Context mContext;
    public String BASE_URL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        BASE_URL=RetrofitClient.BASE_URL;
        mContext = WebViewActivity.this;
        webView=(WebView) findViewById(R.id.webView);
        bar = (ProgressBar) findViewById(R.id.progress1);
        bundle = getIntent().getExtras();
        product_name = bundle.getString("product_name");
        webUrl=BASE_URL+product_name+".php";
        LoadWebView();
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


}
