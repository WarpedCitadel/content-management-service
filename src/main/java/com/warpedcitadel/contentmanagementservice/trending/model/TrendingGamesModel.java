package com.warpedcitadel.contentmanagementservice.trending.model;

import java.util.List;

public class TrendingGamesModel {

    private String gameProfileUUID;
    private String coverImage;
    private String title;
    private String shortDesc;
    private String genre;
    private List<Integer> platformOS;
    private String createdDtm;
    private String displayName;


    public TrendingGamesModel() {

    }

    public TrendingGamesModel(String gameProfileUUID, String coverImage, String title,
                              String shortDesc, String genre, List<Integer> platformOS, String createdDtm,
                              String displayName) {

        this.gameProfileUUID = gameProfileUUID;
        this.coverImage = coverImage;
        this.title = title;
        this.shortDesc = shortDesc;
        this.genre = genre;
        this.platformOS = platformOS;
        this.createdDtm = createdDtm;
        this.displayName = displayName;
    }


    public String getGameProfileUUID() {
        return gameProfileUUID;
    }

    public String getCoverImage() {
        return coverImage;
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

    public List<Integer> getPlatformOS() {
        return platformOS;
    }

    public String getCreatedDtm() {
        return createdDtm;
    }

    public String getDisplayName() {
        return displayName;
    }


    public void setGameProfileUUID(String gameProfileUUID) {
        this.gameProfileUUID = gameProfileUUID;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
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

    public void setPlatformOS(List<Integer> platformOS) {
        this.platformOS = platformOS;
    }

    public void setCreatedDtm(String createdDtm) {
        this.createdDtm = createdDtm;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
