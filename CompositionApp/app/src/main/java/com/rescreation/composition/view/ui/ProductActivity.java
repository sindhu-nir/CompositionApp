package com.rescreation.composition.view.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.rescreation.composition.R;
import com.rescreation.composition.model.Category;
import com.rescreation.composition.model.Config;
import com.rescreation.composition.model.Product;
import com.rescreation.composition.util.NotificationUtils;
import com.rescreation.composition.view.adapter.MainActivityAdapter;
import com.rescreation.composition.view.adapter.ProductActivityAdapter;
import com.rescreation.composition.viewmodel.MainActivityViewModel;
import com.rescreation.composition.viewmodel.ProductActivityViewModel;

import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends AppCompatActivity implements android.widget.SearchView.OnQueryTextListener{

    Bundle bundle;
    String catID="",catName="";

    ProductActivityViewModel productActivityViewModel;

    ArrayList<Product> productArrayList;
    ProductActivityAdapter productActivityAdapter;
    RecyclerView productRecycleView;
    ProgressBar progressBar;
    private LottieAnimationView emptyAnimation;
    private AdView mAdView;
    ImageButton backImageButton;
    TextView titleTextView;
    private android.widget.SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        backImageButton=findViewById(R.id.backImageButton);
        titleTextView=findViewById(R.id.title);
        backImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        bundle = getIntent().getExtras();
        catID = bundle.getString("cat_id");
        catName = bundle.getString("cat_name");
        titleTextView.setText(catName.toUpperCase());

        productRecycleView = findViewById(R.id.productRecycleView);
        progressBar = findViewById(R.id.progressBar);
        productArrayList = new ArrayList<>();
        emptyAnimation = findViewById(R.id.emptyAnimation);
        mSearchView=(android.widget.SearchView)   findViewById(R.id.searchView);



        productActivityViewModel = ViewModelProviders.of(this).get(ProductActivityViewModel.class);
        productActivityViewModel.init(catID);

        setupRecyclerView();
        productActivityViewModel.getProductRepository().observe(this, productResponse -> {
            if (productResponse!=null && productResponse.getSuccess().equals("true")) {
                progressBar.setVisibility(View.GONE);
                List<Product> productList = productResponse.getProducts();
                productArrayList.addAll(productList);
                productActivityAdapter.notifyDataSetChanged();
                emptyAnimation.setVisibility(View.GONE);
                mSearchView.setOnQueryTextListener((SearchView.OnQueryTextListener) this);

            }
            else{
                progressBar.setVisibility(View.GONE);
                Toast.makeText(this, "Nothing Found", Toast.LENGTH_SHORT).show();
                emptyAnimation.setVisibility(View.VISIBLE);

            }
        });


        mSearchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleTextView.setVisibility(View.GONE);
                mSearchView.setMaxWidth(android.R.attr.width);


            }
        });
        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                titleTextView.setVisibility(View.VISIBLE);
                return false;
            }
        });

        // initialize the AdMob app
        MobileAds.initialize(this, getString(R.string.admob_app_id));
        mAdView = findViewById(R.id.adView);
        LoadAdmobAds();
    }

    private void setupRecyclerView() {
        if (productActivityAdapter == null) {
            productActivityAdapter = new ProductActivityAdapter(ProductActivity.this, productArrayList,catName);
            productRecycleView.setLayoutManager(new LinearLayoutManager(this));
            //productRecycleView.setLayoutManager(new GridLayoutManager(this,2));
            productRecycleView.setAdapter(productActivityAdapter);
            productRecycleView.setItemAnimator(new DefaultItemAnimator());
            productRecycleView.setNestedScrollingEnabled(true);
            mSearchView.setOnQueryTextListener((SearchView.OnQueryTextListener) this);

        } else {
            productActivityAdapter.notifyDataSetChanged();
            mSearchView.setOnQueryTextListener((SearchView.OnQueryTextListener) this);

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


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String text = newText;
        productActivityAdapter.filter(text);
        return false;
    }




}
