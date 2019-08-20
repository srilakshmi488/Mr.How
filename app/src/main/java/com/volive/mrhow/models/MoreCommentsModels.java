package com.volive.mrhow.models;

public class MoreCommentsModels {
    String comment_id;

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment_rating() {
        return comment_rating;
    }

    public void setComment_rating(String comment_rating) {
        this.comment_rating = comment_rating;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    String user_id;
    String comment;
    String comment_rating;

    public MoreCommentsModels(String comment_id, String user_id, String comment,
                              String comment_rating, String created_on, String time) {
        this.comment_id = comment_id;
        this.user_id = user_id;
        this.comment = comment;
        this.comment_rating = comment_rating;
        this.created_on = created_on;
        this.time = time;
    }

    String created_on;
    String time;

}
