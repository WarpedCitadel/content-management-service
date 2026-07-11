package com.warpedcitadel.contentmanagementservice.profile.model;

import java.util.List;

public class GameFileDetailsModel {

    String browserGameUUID;
    List<String> fileName;


    public GameFileDetailsModel() {

    }

    public GameFileDetailsModel(String browserGameUUID, List<String> fileName) {
        this.browserGameUUID = browserGameUUID;
        this.fileName = fileName;
    }


    public String getBrowserGameUUID() {
        return browserGameUUID;
    }

    public List<String> getFileName() {
        return fileName;
    }

    public void setBrowserGameUUID(String browserGameUUID) {
        this.browserGameUUID = browserGameUUID;
    }

    public void setFileName(List<String> fileName) {
        this.fileName = fileName;
    }
}
