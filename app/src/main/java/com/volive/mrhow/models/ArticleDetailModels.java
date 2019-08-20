package com.volive.mrhow.models;

public class ArticleDetailModels {
    String details_id;

    public ArticleDetailModels(String details_id, String detail_image, String detail_thumb, String detail_image_type, String detail_description_en) {
        this.details_id = details_id;
        this.detail_image = detail_image;
        this.detail_thumb = detail_thumb;
        this.detail_image_type = detail_image_type;
        this.detail_description_en = detail_description_en;
    }

    String detail_image;

    public String getDetails_id() {
        return details_id;
    }

    public void setDetails_id(String details_id) {
        this.details_id = details_id;
    }

    public String getDetail_image() {
        return detail_image;
    }

    public void setDetail_image(String detail_image) {
        this.detail_image = detail_image;
    }

    public String getDetail_thumb() {
        return detail_thumb;
    }

    public void setDetail_thumb(String detail_thumb) {
        this.detail_thumb = detail_thumb;
    }

    public String getDetail_image_type() {
        return detail_image_type;
    }

    public void setDetail_image_type(String detail_image_type) {
        this.detail_image_type = detail_image_type;
    }

    public String getDetail_description_en() {
        return detail_description_en;
    }

    public void setDetail_description_en(String detail_description_en) {
        this.detail_description_en = detail_description_en;
    }

    String detail_thumb;
    String detail_image_type;
    String detail_description_en;
}
