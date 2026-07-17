package com.warpedcitadel.contentmanagementservice.profile.model;

import java.util.List;

public class GameFileDetailsModel {

    List<String> fileName;
    List<Integer> fileOS;


    public GameFileDetailsModel() {

    }

    public GameFileDetailsModel(List<String> fileName, List<Integer> fileOS) {
        this.fileName = fileName;
        this.fileOS = fileOS;
    }


    public List<String> getFileName() {
        return fileName;
    }

    public List<Integer> getFileOS() {
        return fileOS;
    }


    public void setFileName(List<String> fileName) {
        this.fileName = fileName;
    }

    public void setFileOS(List<Integer> fileOS) {
        this.fileOS = fileOS;
    }
}
