package com.warpedcitadel.contentmanagementservice.profile.model;

import java.util.List;

public class GameProfileDetailsModel {

    private String gameProfileUUID;
    private String title;
    private String description;
    private String genreType;
    private String gameType;
    private List<Integer> platformOS;
    private String createdDtm;
    private String displayName;
    private String userUUID;
    private GameProfileImagesModel gameProfileImagesModel;
    private GameFileDetailsModel gameFileDetailsModel;


    public GameProfileDetailsModel() {

    }

    public GameProfileDetailsModel(String gameProfileUUID, String title, String description,
                                   String genreType, String gameType, List<Integer> platformOS,
                                   String createdDtm, String displayName, String userUUID,
                                   GameProfileImagesModel gameProfileImagesModel, GameFileDetailsModel gameFileDetailsModel) {

        this.gameProfileUUID = gameProfileUUID;
        this.title = title;
        this.description = description;
        this.genreType = genreType;
        this.gameType = gameType;
        this.platformOS = platformOS;
        this.createdDtm = createdDtm;
        this.displayName = displayName;
        this.userUUID = userUUID;
        this.gameProfileImagesModel = gameProfileImagesModel;
        this.gameFileDetailsModel = gameFileDetailsModel;
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

    public List<Integer> getPlatformOS() {
        return platformOS;
    }

    public String getCreatedDtm() {
        return createdDtm;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getUserUUID() {
        return userUUID;
    }

    public GameProfileImagesModel getGameProfileImagesModel() {
        return gameProfileImagesModel;
    }

    public GameFileDetailsModel getGameFileDetailsModel() {
        return gameFileDetailsModel;
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

    public void setPlatformOS(List<Integer> platformOS) {
        this.platformOS = platformOS;
    }

    public void setCreatedDtm(String createdDtm) {
        this.createdDtm = createdDtm;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setUserUUID(String userUUID) {
        this.userUUID = userUUID;
    }

    public void setGameProfileImagesModel(GameProfileImagesModel gameProfileImagesModel) {
        this.gameProfileImagesModel = gameProfileImagesModel;
    }

    public void setGameFileDetailsModel(GameFileDetailsModel gameFileDetailsModel) {
        this.gameFileDetailsModel = gameFileDetailsModel;
    }
}
