package com.rescreation.composition.model;

public class Product {

    private String product_id;
    private String category_id;
    private String product_name;
    private String product_description;


    public Product(String product_id, String category_id, String product_name, String product_description) {
        this.product_id = product_id;
        this.category_id = category_id;
        this.product_name = product_name;
        this.product_description = product_description;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getProduct_description() {
        return product_description;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public void setProduct_description(String product_description) {
        this.product_description = product_description;
    }
}
