package com.volive.mrhow.models;

import java.util.ArrayList;

public class CommentsModels {
    String article_comment_id;
    String main_comment;
    String name;
    String profile_pic;

    public ArrayList<SubCommentsModels> getSubCommentsModels() {
        return subCommentsModels;
    }

    public void setSubCommentsModels(ArrayList<SubCommentsModels> subCommentsModels) {
        this.subCommentsModels = subCommentsModels;
    }

    ArrayList<SubCommentsModels> subCommentsModels;

    public CommentsModels(String article_comment_id, String main_comment, String name, String profile_pic,
                          String commented_on, String time, String article_comment_likes, String article_sub_comment_count,
                          ArrayList<SubCommentsModels> subCommentsModels) {
        this.article_comment_id = article_comment_id;
        this.main_comment = main_comment;
        this.name = name;
        this.profile_pic = profile_pic;
        this.commented_on = commented_on;
        this.time = time;
        this.article_comment_likes = article_comment_likes;
        this.article_sub_comment_count = article_sub_comment_count;
        this.subCommentsModels=subCommentsModels;
    }

    String commented_on;
    String time;

    public String getArticle_comment_id() {
        return article_comment_id;
    }

    public void setArticle_comment_id(String article_comment_id) {
        this.article_comment_id = article_comment_id;
    }

    public String getMain_comment() {
        return main_comment;
    }

    public void setMain_comment(String main_comment) {
        this.main_comment = main_comment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getCommented_on() {
        return commented_on;
    }

    public void setCommented_on(String commented_on) {
        this.commented_on = commented_on;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getArticle_comment_likes() {
        return article_comment_likes;
    }

    public void setArticle_comment_likes(String article_comment_likes) {
        this.article_comment_likes = article_comment_likes;
    }

    public String getArticle_sub_comment_count() {
        return article_sub_comment_count;
    }

    public void setArticle_sub_comment_count(String article_sub_comment_count) {
        this.article_sub_comment_count = article_sub_comment_count;
    }

    String article_comment_likes;
    String article_sub_comment_count;

}
