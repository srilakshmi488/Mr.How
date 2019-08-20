package com.volive.mrhow.models;

public class SubCommentsModels {
    public String getSub_comment() {
        return sub_comment;
    }

    public void setSub_comment(String sub_comment) {
        this.sub_comment = sub_comment;
    }

    public String getSubcoomentname() {
        return subcoomentname;
    }

    public void setSubcoomentname(String subcoomentname) {
        this.subcoomentname = subcoomentname;
    }

    String sub_comment;

    public SubCommentsModels(String sub_comment, String subcoomentname) {
        this.sub_comment = sub_comment;
        this.subcoomentname = subcoomentname;
    }

    String subcoomentname;
}
