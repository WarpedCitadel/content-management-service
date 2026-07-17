package com.warpedcitadel.contentmanagementservice.profile.model;

import java.util.List;

public class GameProfileImagesModel {

    private String coverImage;
    private List<String> gameImage;

    public GameProfileImagesModel() {

    }

    public GameProfileImagesModel(String coverImage, List<String> gameImage) {

        this.coverImage = coverImage;
        this.gameImage = gameImage;
    }


    public String getCoverImg() {
        return coverImage;
    }

    public List<String> getGameImg() {
        return gameImage;
    }


    public void setCoverImg(String coverImage) {
        this.coverImage = coverImage;
    }

    public void setGameImg(List<String> gameImage) {
        this.gameImage = gameImage;
    }
}
