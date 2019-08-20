package com.volive.mrhow.models;

public class NotificationModels {
    String title;
    String text;
    String type;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSeen_status() {
        return seen_status;
    }

    public void setSeen_status(String seen_status) {
        this.seen_status = seen_status;
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

    String seen_status;
    String name;
    String profile_pic;

    public NotificationModels(String title, String text, String type, String seen_status, String name, String profile_pic, String created_on, String time) {
        this.title = title;
        this.text = text;
        this.type = type;
        this.seen_status = seen_status;
        this.name = name;
        this.profile_pic = profile_pic;
        this.created_on = created_on;
        this.time = time;
    }

    String created_on;
    String time;

}
