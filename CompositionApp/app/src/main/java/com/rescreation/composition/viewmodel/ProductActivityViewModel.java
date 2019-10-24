package com.rescreation.composition.viewmodel;

import android.app.Application;

import com.rescreation.composition.model.CategoryResponse;
import com.rescreation.composition.model.ProductResponse;
import com.rescreation.composition.model.repository.CategoryRepository;
import com.rescreation.composition.model.repository.ProductRepository;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class ProductActivityViewModel extends AndroidViewModel {
    public ProductActivityViewModel(@NonNull Application application) {
        super(application);
    }


    private MutableLiveData<ProductResponse> mutableLiveData;
    private ProductRepository productRepository;

    public void init(String cat_id){
        if (mutableLiveData != null){
            return;
        }
        productRepository = ProductRepository.getInstance();
        mutableLiveData = productRepository.getProductList(cat_id);

    }

    public LiveData<ProductResponse> getProductRepository() {
        return mutableLiveData;
    }

}
