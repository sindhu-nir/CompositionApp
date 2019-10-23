package com.rescreation.composition.view.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.rescreation.composition.R;
import com.rescreation.composition.model.Category;
import com.rescreation.composition.model.CategoryResponse;
import com.rescreation.composition.view.adapter.MainActivityAdapter;
import com.rescreation.composition.viewmodel.MainActivityViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    MainActivityViewModel mainActivityViewModel;

    ArrayList<Category> categoryArrayList;
    MainActivityAdapter categoryAdapter;
    RecyclerView categoryRecycleView;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        categoryRecycleView = findViewById(R.id.menuRecycleView);
        progressBar = findViewById(R.id.progressBar);
        categoryArrayList = new ArrayList<>();


        mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        mainActivityViewModel.init();

        setupRecyclerView();

        mainActivityViewModel.getCategoryRepository().observe(this, categoryResponse -> {
            if (categoryResponse!=null) {
                progressBar.setVisibility(View.GONE);
                List<Category> categoryList = categoryResponse.getCategories();
                categoryArrayList.addAll(categoryList);
                categoryAdapter.notifyDataSetChanged();

            }
            else{
                progressBar.setVisibility(View.GONE);
                Toast.makeText(this, "Nothing Found", Toast.LENGTH_SHORT).show();

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


}
