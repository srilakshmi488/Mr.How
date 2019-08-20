package com.volive.mrhow.models;

public class StudioModels {
  String article_id;
    String article_title;

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    String thumbnail;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    String date;

    public StudioModels(String article_id, String article_title, String article_description, String article_image,
                        String image_type, String writer, String created_on, String likes_count, String comments_count,
                        String views_count,String date,String thumbnail) {
        this.article_id = article_id;
        this.article_title = article_title;
        this.article_description = article_description;
        this.article_image = article_image;
        this.image_type = image_type;
        this.writer = writer;
        this.created_on = created_on;
        this.likes_count = likes_count;
        this.comments_count = comments_count;
        this.views_count = views_count;
        this.date= date;
        this.thumbnail=thumbnail;
    }

    String article_description;
    String article_image;
    String image_type;

    public String getArticle_id() {
        return article_id;
    }

    public void setArticle_id(String article_id) {
        this.article_id = article_id;
    }

    public String getArticle_title() {
        return article_title;
    }

    public void setArticle_title(String article_title) {
        this.article_title = article_title;
    }

    public String getArticle_description() {
        return article_description;
    }

    public void setArticle_description(String article_description) {
        this.article_description = article_description;
    }

    public String getArticle_image() {
        return article_image;
    }

    public void setArticle_image(String article_image) {
        this.article_image = article_image;
    }

    public String getImage_type() {
        return image_type;
    }

    public void setImage_type(String image_type) {
        this.image_type = image_type;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public String getLikes_count() {
        return likes_count;
    }

    public void setLikes_count(String likes_count) {
        this.likes_count = likes_count;
    }

    public String getComments_count() {
        return comments_count;
    }

    public void setComments_count(String comments_count) {
        this.comments_count = comments_count;
    }

    public String getViews_count() {
        return views_count;
    }

    public void setViews_count(String views_count) {
        this.views_count = views_count;
    }

    String writer;
    String created_on;
    String likes_count;
    String comments_count;
    String views_count;
}
