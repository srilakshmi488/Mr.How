package com.volive.mrhow.models;

public class ReviewsModels {
    public String getReview_id() {
        return review_id;
    }

    public void setReview_id(String review_id) {
        this.review_id = review_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public String getReviewname() {
        return reviewname;
    }

    public void setReviewname(String reviewname) {
        this.reviewname = reviewname;
    }

    String review_id;
    String user_id;

    public ReviewsModels(String review_id, String user_id, String review, String rating, String created_on, String reviewname) {
        this.review_id = review_id;
        this.user_id = user_id;
        this.review = review;
        this.rating = rating;
        this.created_on = created_on;
        this.reviewname = reviewname;
    }

    String review;
    String rating;
    String created_on;
    String reviewname;
}
