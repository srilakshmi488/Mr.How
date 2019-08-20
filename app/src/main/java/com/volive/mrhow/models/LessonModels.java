package com.volive.mrhow.models;

public class LessonModels {

    public LessonModels(String lesson_name, String video, String lesson_thumbnail, String lesson_id,String lesson_type) {
        this.lesson_name = lesson_name;
        this.video = video;
        this.lesson_thumbnail = lesson_thumbnail;
        this.lesson_id = lesson_id;
        this.lesson_type=lesson_type;
    }

    String lesson_name;

    public String getLesson_type() {
        return lesson_type;
    }

    public void setLesson_type(String lesson_type) {
        this.lesson_type = lesson_type;
    }

    String lesson_type;

    public String getLesson_name() {
        return lesson_name;
    }

    public void setLesson_name(String lesson_name) {
        this.lesson_name = lesson_name;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getLesson_thumbnail() {
        return lesson_thumbnail;
    }

    public void setLesson_thumbnail(String lesson_thumbnail) {
        this.lesson_thumbnail = lesson_thumbnail;
    }

    public String getLesson_id() {
        return lesson_id;
    }

    public void setLesson_id(String lesson_id) {
        this.lesson_id = lesson_id;
    }

    String video;
    String lesson_thumbnail;
    String lesson_id;

}
