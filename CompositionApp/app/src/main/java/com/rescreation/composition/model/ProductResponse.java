package com.rescreation.composition.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductResponse {

    private String success;
    private String status;
    @SerializedName("PRODUCTS")
    List<Product> Products;

    public ProductResponse(String success, String status, List<Product> products) {
        this.success = success;
        this.status = status;
        Products = products;
    }


    public String getSuccess() {
        return success;
    }

    public String getStatus() {
        return status;
    }

    public List<Product> getProducts() {
        return Products;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setProducts(List<Product> products) {
        Products = products;
    }
}
