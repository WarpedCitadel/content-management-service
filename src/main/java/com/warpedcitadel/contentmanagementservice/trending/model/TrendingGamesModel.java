package com.warpedcitadel.contentmanagementservice.trending.model;

import java.util.List;

public class TrendingGamesModel {

    private String gameProfileUUID;
    private String coverImg;
    private String title;
    private String shortDesc;
    private String genre;
    private List<String> platformOS;
    private String createdDtm;


    public TrendingGamesModel() {

    }

    public TrendingGamesModel(String gameProfileUUID, String coverImg, String title,
                              String shortDesc, String genre, List<String> platformOS, String createdDtm) {

        this.gameProfileUUID = gameProfileUUID;
        this.coverImg = coverImg;
        this.title = title;
        this.shortDesc = shortDesc;
        this.genre = genre;
        this.platformOS = platformOS;
        this.createdDtm = createdDtm;
    }


    public String getGameProfileUUID() {
        return gameProfileUUID;
    }

    public String getCoverImg() {
        return coverImg;
    }

    public String getTitle() {
        return title;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public String getGenre() {
        return genre;
    }

    public List<String> getPlatformOS() {
        return platformOS;
    }

    public String getCreatedDtm() {
        return createdDtm;
    }


    public void setGameProfileUUID(String gameProfileUUID) {
        this.gameProfileUUID = gameProfileUUID;
    }

    public void setCoverImgUUID(String coverImg) {
        this.coverImg= coverImg;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setPlatformOS(List<String> platformOS) {
        this.platformOS = platformOS;
    }

    public void setCreatedDtm(String createdDtm) {
        this.createdDtm = createdDtm;
    }
}
