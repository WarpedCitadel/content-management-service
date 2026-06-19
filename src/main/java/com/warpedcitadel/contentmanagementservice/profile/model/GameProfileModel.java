package com.warpedcitadel.contentmanagementservice.profile.model;

public class GameProfileModel {

    private String userUUID;
    private String title;
    private String shortDesc;
    private String description;
    private int gameGenre;
    private int gameType;
    private Integer[] platformOS;


    public GameProfileModel() {

    }

    public GameProfileModel(String userUUID, String title, String shortDesc, String description,
                     int gameGenre, int gameType, Integer[] platformOS) {

        this.userUUID = userUUID;
        this.title = title;
        this.shortDesc = shortDesc;
        this.description = description;
        this.gameGenre = gameGenre;
        this.gameType = gameType;
        this.platformOS = platformOS;
    }


    public String getUserUUID() {
        return userUUID;
    }

    public String getTitle() {
        return title;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public String getDescription() {
        return description;
    }

    public int getGameGenre() {
        return gameGenre;
    }

    public int getGameType() {
        return gameType;
    }

    public Integer[] getPlatformOS() {
        return platformOS;
    }


    public void setUserUUID(String userUUID) {
        this.userUUID = userUUID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setGameGenre(int gameGenre) {
        this.gameGenre = gameGenre;
    }

    public void setGameType(int gameType) {
        this.gameType = gameType;
    }

    public void setPlatformOS(Integer[] platformOS) {
        this.platformOS = platformOS;
    }
}
