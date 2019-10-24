package com.rescreation.composition.model.repository;

import com.rescreation.composition.model.CategoryResponse;
import com.rescreation.composition.model.ProductResponse;

import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductRepository {

    public static ProductRepository productRepository;

    public static ProductRepository getInstance(){
        if (productRepository == null){
            productRepository = new ProductRepository();
        }
        return productRepository;
    }

    public   RetrofitService retrofitService;

    public ProductRepository(){
        retrofitService = RetrofitClient.cteateService(RetrofitService.class);

    }


    public MutableLiveData<ProductResponse> getProductList(String cat_id){
        MutableLiveData<ProductResponse>  mutableLiveData = new MutableLiveData<>();
        retrofitService.GetProduct(cat_id).enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call,
                                   Response<ProductResponse> response) {
                if (response.isSuccessful()){
                    mutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                mutableLiveData.setValue(null);
            }
        });
        return mutableLiveData;
    }


}
