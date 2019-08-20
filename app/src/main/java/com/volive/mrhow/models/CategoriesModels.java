package com.volive.mrhow.models;

public class CategoriesModels {
    String category_id;

    public CategoriesModels(String category_id, String category_name, String icon) {
        this.category_id = category_id;
        this.category_name = category_name;
        this.icon = icon;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    String category_name;
    String icon;

}
