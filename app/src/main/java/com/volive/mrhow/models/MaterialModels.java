package com.volive.mrhow.models;

public class MaterialModels {
    public String getMaterial_name() {
        return material_name;
    }

    public void setMaterial_name(String material_name) {
        this.material_name = material_name;
    }

    public String getMaterial_file() {
        return material_file;
    }

    public void setMaterial_file(String material_file) {
        this.material_file = material_file;
    }

    String material_name;

    public MaterialModels(String material_name, String material_file,String material_type, String material_thumb) {
        this.material_name = material_name;
        this.material_file = material_file;
        this.material_type=material_type;
        this.material_thumb=material_thumb;
    }

    String material_file;
    String material_type;

    public String getMaterial_type() {
        return material_type;
    }

    public void setMaterial_type(String material_type) {
        this.material_type = material_type;
    }

    public String getMaterial_thumb() {
        return material_thumb;
    }

    public void setMaterial_thumb(String material_thumb) {
        this.material_thumb = material_thumb;
    }

    String material_thumb;
}
