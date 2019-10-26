package com.rescreation.composition.model.repository;

import com.rescreation.composition.model.CategoryResponse;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryRepository {


    public  static CategoryRepository categoryRepository;

    public static CategoryRepository getInstance(){
        if (categoryRepository == null){
            categoryRepository = new CategoryRepository();
        }
        return categoryRepository;
    }

    public   RetrofitService retrofitService;


    public CategoryRepository(){
        retrofitService = RetrofitClient.cteateService(RetrofitService.class);
        //mutableLiveData= new MutableLiveData<>();

    }




    public MutableLiveData<CategoryResponse> getCategoryList(){
        MutableLiveData<CategoryResponse>  mutableLiveData = new MutableLiveData<>();
        retrofitService.Categories().enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call,
                                   Response<CategoryResponse> response) {
                if (response.isSuccessful()){
                    mutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {
                mutableLiveData.setValue(null);
            }
        });
        return mutableLiveData;
    }

}
