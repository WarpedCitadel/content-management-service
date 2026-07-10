package com.warpedcitadel.contentmanagementservice.profile.model;

import java.util.List;

public class GameFileDetailsModel {

    String browserGameUUID;
    List<String> fileName;
    List<String> fileUUID;


    public GameFileDetailsModel() {

    }

    public GameFileDetailsModel(String browserGameUUID, List<String> fileName, List<String> fileUUID) {
        this.browserGameUUID = browserGameUUID;
        this.fileName = fileName;
        this.fileUUID = fileUUID;
    }


    public String getBrowserGameUUID() {
        return browserGameUUID;
    }

    public List<String> getFileName() {
        return fileName;
    }

    public List<String> getFileUUID() {
        return fileUUID;
    }


    public void setBrowserGameUUID(String browserGameUUID) {
        this.browserGameUUID = browserGameUUID;
    }

    public void setFileName(List<String> fileName) {
        this.fileName = fileName;
    }

    public void setFileUUID(List<String> fileUUID) {
        this.fileUUID = fileUUID;
    }
}
