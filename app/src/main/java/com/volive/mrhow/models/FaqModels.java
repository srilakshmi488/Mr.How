package com.volive.mrhow.models;

public class FaqModels {
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

    public FaqModels(String title, String text) {
        this.title = title;
        this.text = text;
    }

    String title,text;

}
