package com.volive.mrhow.models;

public class StudentProjectModels {
    String project_id;

    public String getProject_thumbnail() {
        return project_thumbnail;
    }

    public void setProject_thumbnail(String project_thumbnail) {
        this.project_thumbnail = project_thumbnail;
    }

    String project_thumbnail;

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    public String getProject_banner() {
        return project_banner;
    }

    public void setProject_banner(String project_banner) {
        this.project_banner = project_banner;
    }

    public String getBanner_type() {
        return banner_type;
    }

    public void setBanner_type(String banner_type) {
        this.banner_type = banner_type;
    }

    String project_banner;

    public StudentProjectModels(String project_id, String project_banner, String banner_type,String project_thumbnail) {
        this.project_id = project_id;
        this.project_banner = project_banner;
        this.banner_type = banner_type;
        this.project_thumbnail=project_thumbnail;
    }

    String banner_type;
}
