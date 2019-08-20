package com.volive.mrhow.models;

public class BannersModels {
    String course_id;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    String currency;

    public BannersModels(String course_id, String category_name, String course_title, String price, String cover, String ratings,String currency) {
        this.course_id = course_id;
        this.category_name = category_name;
        this.course_title = course_title;
        this.price = price;
        this.cover = cover;
        this.ratings = ratings;
        this.currency=currency;
    }

    String category_name;

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCourse_title() {
        return course_title;
    }

    public void setCourse_title(String course_title) {
        this.course_title = course_title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getRatings() {
        return ratings;
    }

    public void setRatings(String ratings) {
        this.ratings = ratings;
    }

    String course_title;
    String price;
    String cover;
    String ratings;
}
