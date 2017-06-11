package com.arajago6.assessmentapp.data_model;

public class ActionData {

    private String title = "";
    private String subTitle = "";
    private String description = "This is some detailed description";
    private int cubeCount = 1;
    private String imgName = "";

    public ActionData(){}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCubeCount() {
        return cubeCount;
    }

    public void setCubeCount(int cubeCount) {
        this.cubeCount = cubeCount;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }
}
