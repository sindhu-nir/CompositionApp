package com.rescreation.composition.view.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.rescreation.composition.R;
import com.rescreation.composition.model.Category;
import com.rescreation.composition.model.Product;
import com.rescreation.composition.view.adapter.MainActivityAdapter;
import com.rescreation.composition.view.adapter.ProductActivityAdapter;
import com.rescreation.composition.viewmodel.MainActivityViewModel;
import com.rescreation.composition.viewmodel.ProductActivityViewModel;

import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends AppCompatActivity {

    Bundle bundle;
    String catID="";

    ProductActivityViewModel productActivityViewModel;

    ArrayList<Product> productArrayList;
    ProductActivityAdapter productActivityAdapter;
    RecyclerView productRecycleView;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        bundle = getIntent().getExtras();
        catID = bundle.getString("cat_id");

        productRecycleView = findViewById(R.id.productRecycleView);
        progressBar = findViewById(R.id.progressBar);
        productArrayList = new ArrayList<>();


        productActivityViewModel = ViewModelProviders.of(this).get(ProductActivityViewModel.class);
        productActivityViewModel.init(catID);

        setupRecyclerView();
        productActivityViewModel.getProductRepository().observe(this, productResponse -> {
            if (productResponse!=null && productResponse.getSuccess().equals("true")) {
                progressBar.setVisibility(View.GONE);
                List<Product> productList = productResponse.getProducts();
                productArrayList.addAll(productList);
                productActivityAdapter.notifyDataSetChanged();

            }
            else{
                progressBar.setVisibility(View.GONE);
                Toast.makeText(this, "Nothing Found", Toast.LENGTH_SHORT).show();

            }
        });




    }

    private void setupRecyclerView() {
        if (productActivityAdapter == null) {
            productActivityAdapter = new ProductActivityAdapter(ProductActivity.this, productArrayList);
            //categoryRecycleView.setLayoutManager(new LinearLayoutManager(this));
            productRecycleView.setLayoutManager(new GridLayoutManager(this,2));
            productRecycleView.setAdapter(productActivityAdapter);
            productRecycleView.setItemAnimator(new DefaultItemAnimator());
            productRecycleView.setNestedScrollingEnabled(true);
        } else {
            productActivityAdapter.notifyDataSetChanged();
        }
    }

}
