package com.warpedcitadel.contentmanagementservice.profile.model;

import java.util.List;

public class GameProfileImagesModel {

    private String coverImg;
    private List<String> gameImg;

    public GameProfileImagesModel() {

    }

    public GameProfileImagesModel(String coverImg, List<String> gameImg) {

        this.coverImg = coverImg;
        this.gameImg = gameImg;
    }


    public String getCoverImg() {
        return coverImg;
    }

    public List<String> getGameImg() {
        return gameImg;
    }


    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }

    public void setGameImg(List<String> gameImg) {
        this.gameImg = gameImg;
    }
}
