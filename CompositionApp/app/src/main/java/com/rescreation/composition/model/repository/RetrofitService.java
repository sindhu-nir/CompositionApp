package com.rescreation.composition.model.repository;

import com.rescreation.composition.model.CategoryResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitService {

    @GET("getCategoriesList.php")
    Call<CategoryResponse> Categories();
}
