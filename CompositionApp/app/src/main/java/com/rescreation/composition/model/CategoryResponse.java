package com.rescreation.composition.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryResponse {
    private String success;
    private String status;
    @SerializedName("CATEGORIES")
    List<Category> Categories;

    public String getSuccess() {
        return success;
    }

    public String getStatus() {
        return status;
    }

    public List<Category> getCategories() {
        return Categories;
    }


    public void setSuccess(String success) {
        this.success = success;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCategories(List<Category> categories) {
        Categories = categories;
    }
}
