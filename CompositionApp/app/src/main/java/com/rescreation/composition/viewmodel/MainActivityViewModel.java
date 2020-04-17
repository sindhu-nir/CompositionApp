package com.rescreation.composition.viewmodel;

import android.app.Application;


import com.rescreation.composition.model.CategoryResponse;
import com.rescreation.composition.model.repository.CategoryRepository;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainActivityViewModel extends AndroidViewModel {
    public MainActivityViewModel(@NonNull Application application) {
        super(application);
    }

    private MutableLiveData<CategoryResponse> mutableLiveData;
    private CategoryRepository categoryRepository;

    public void init(){
        if (mutableLiveData != null){
            return;
        }
        categoryRepository = CategoryRepository.getInstance();
        mutableLiveData = categoryRepository.getCategoryList();

    }

    public LiveData<CategoryResponse> getCategoryRepository() {
        return mutableLiveData;
    }



}