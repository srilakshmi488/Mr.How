package com.volive.mrhow.models;

public class MyDownloadModels {
    String course_id;

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public String getCourse_title() {
        return course_title;
    }

    public void setCourse_title(String course_title) {
        this.course_title = course_title;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getFile_type() {
        return file_type;
    }

    public void setFile_type(String file_type) {
        this.file_type = file_type;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    String course_title;
    String file_name;
    String file;
    String file_type;

    public String getDownload_id() {
        return download_id;
    }

    public void setDownload_id(String download_id) {
        this.download_id = download_id;
    }

    String download_id;

    public MyDownloadModels(String course_id, String course_title, String file_name, String file, String file_type,
                            String thumbnail,String download_id) {
        this.course_id = course_id;
        this.course_title = course_title;
        this.file_name = file_name;
        this.file = file;
        this.file_type = file_type;
        this.thumbnail = thumbnail;
        this.download_id=download_id;
    }

    String thumbnail;
}
