package com.rescreation.composition.view.adapter;

import android.graphics.Bitmap;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class myWebViewClient extends WebViewClient {

    ProgressBar bar;

    public myWebViewClient(ProgressBar bar) {
        this.bar = bar;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        // TODO Auto-generated method stubsuper.onPageStarted(view, url, favicon);


        bar.setVisibility(View.VISIBLE);

        super.onPageStarted(view, url, favicon);
    }
    @Override
    public void onPageFinished(WebView view, String url)
    {


        bar.setVisibility(View.GONE);
        super.onPageFinished(view, url);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;

    }
}