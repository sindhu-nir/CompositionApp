package com.rescreation.composition.model;

public class Category {

    private String id;
    private String menu_name;
    private String menu_image;
    private String menu_description;


    public Category(String id, String menu_name, String menu_image, String menu_description) {
        this.id = id;
        this.menu_name = menu_name;
        this.menu_image = menu_image;
        this.menu_description = menu_description;
    }

    public String getId() {
        return id;
    }

    public String getMenu_name() {
        return menu_name;
    }

    public String getMenu_image() {
        return menu_image;
    }

    public String getMenu_description() {
        return menu_description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMenu_name(String menu_name) {
        this.menu_name = menu_name;
    }

    public void setMenu_image(String menu_image) {
        this.menu_image = menu_image;
    }

    public void setMenu_description(String menu_description) {
        this.menu_description = menu_description;
    }
}
