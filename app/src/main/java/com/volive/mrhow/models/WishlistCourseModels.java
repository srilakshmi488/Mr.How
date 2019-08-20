package com.volive.mrhow.models;

public class WishlistCourseModels {
    public WishlistCourseModels(String course_id, String category_name, String course_title, String price, String offer_price,
                                String cover, String cover_type, String total_ratings, String purchased, String tags, String duration,String thumbnail,
                                String currency) {
        this.course_id = course_id;
        this.category_name = category_name;
        this.course_title = course_title;
        this.price = price;
        this.offer_price = offer_price;
        this.cover = cover;
        this.cover_type = cover_type;
        this.total_ratings = total_ratings;
        this.purchased = purchased;
        this.tags = tags;
        this.duration = duration;
        this.thumbnail=thumbnail;
        this.currency=currency;
    }

    String course_id;
    String category_name;
    String course_title;
    String price;
    String offer_price;
    String cover;
    String cover_type;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    String currency;

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    String thumbnail;

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

    public String getOffer_price() {
        return offer_price;
    }

    public void setOffer_price(String offer_price) {
        this.offer_price = offer_price;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getCover_type() {
        return cover_type;
    }

    public void setCover_type(String cover_type) {
        this.cover_type = cover_type;
    }

    public String getTotal_ratings() {
        return total_ratings;
    }

    public void setTotal_ratings(String total_ratings) {
        this.total_ratings = total_ratings;
    }

    public String getPurchased() {
        return purchased;
    }

    public void setPurchased(String purchased) {
        this.purchased = purchased;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    String total_ratings;
    String purchased;
    String tags;
    String duration;

}
