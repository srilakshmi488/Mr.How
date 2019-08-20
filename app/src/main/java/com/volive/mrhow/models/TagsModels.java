package com.volive.mrhow.models;

public class TagsModels {
    public TagsModels(String hashtag_id, String name) {
        this.hashtag_id = hashtag_id;
        this.name = name;
    }

    public String getHashtag_id() {
        return hashtag_id;
    }

    public void setHashtag_id(String hashtag_id) {
        this.hashtag_id = hashtag_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String hashtag_id,name;
}
