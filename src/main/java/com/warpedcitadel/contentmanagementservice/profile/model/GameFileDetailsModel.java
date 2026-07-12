package com.warpedcitadel.contentmanagementservice.profile.model;

import java.util.List;

public class GameFileDetailsModel {

    String browserFileName;
    List<String> fileName;


    public GameFileDetailsModel() {

    }

    public GameFileDetailsModel(String browserFileName, List<String> fileName) {
        this.browserFileName = browserFileName;
        this.fileName = fileName;
    }


    public String getBrowserFileName() {
        return browserFileName;
    }

    public List<String> getFileName() {
        return fileName;
    }

    public void setBrowserFileName(String browserFileName) {
        this.browserFileName = browserFileName;
    }

    public void setFileName(List<String> fileName) {
        this.fileName = fileName;
    }
}
