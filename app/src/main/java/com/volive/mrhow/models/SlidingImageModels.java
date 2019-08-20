package com.volive.mrhow.models;

public class SlidingImageModels {
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

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

    String image;
    String title;

    public SlidingImageModels(String image, String title, String text) {
        this.image = image;
        this.title = title;
        this.text = text;
    }

    String text;
}
