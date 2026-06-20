package com.warpedcitadel.contentmanagementservice.profile.model;

import java.util.List;

public class GameProfileDetailsModel {

    private String gameProfileUUID;
    private String title;
    private String description;
    private String genreType;
    private String gameType;
    private List<String> platformOS;
    private String createdDtm;
    private String coverImg;
    private List<String> gameImg;
    private String displayName;
    private String userUUID;


    public GameProfileDetailsModel() {

    }

    public GameProfileDetailsModel(String gameProfileUUID, String title, String description,
                                   String genreType, String gameType, List<String> platformOS,
                                   String createdDtm, String coverImg, List<String> gameImg,
                                   String displayName, String userUUID) {

        this.gameProfileUUID = gameProfileUUID;
        this.title = title;
        this.description = description;
        this.genreType = genreType;
        this.gameType = gameType;
        this.platformOS = platformOS;
        this.createdDtm = createdDtm;
        this.coverImg = coverImg;
        this.gameImg = gameImg;
        this.displayName = displayName;
        this.userUUID = userUUID;
    }

    public String getGameProfileUUID() {
        return gameProfileUUID;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getGenreType() {
        return genreType;
    }

    public String getGameType() {
        return gameType;
    }

    public List<String> getPlatformOS() {
        return platformOS;
    }

    public String getCreatedDtm() {
        return createdDtm;
    }

    public String getCoverImg() {
        return coverImg;
    }

    public List<String> getGameImg() {
        return gameImg;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getUserUUID() {
        return userUUID;
    }


    public void setGameProfileUUID(String gameProfileUUID) {
        this.gameProfileUUID = gameProfileUUID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setGenreType(String genreType) {
        this.genreType = genreType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public void setPlatformOS(List<String> platformOS) {
        this.platformOS = platformOS;
    }

    public void setCreatedDtm(String createdDtm) {
        this.createdDtm = createdDtm;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }

    public void setGameImg(List<String> gameImg) {
        this.gameImg = gameImg;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setUserUUID(String userUUID) {
        this.userUUID = userUUID;
    }
}
