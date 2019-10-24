package com.rescreation.composition.model.repository;

import com.rescreation.composition.model.CategoryResponse;
import com.rescreation.composition.model.ProductResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitService {

    @GET("getCategoriesList.php")
    Call<CategoryResponse> Categories();


    @GET("getProductList.php")
        //@FormUrlEncoded
    Call<ProductResponse> GetProduct(@Query("cat_id") String cat_id);
}
