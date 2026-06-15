package com.warpedcitadel.contentmanagementservice.trending.model;

public class TrendingGamesModel {

    private String gameProfileUUID;
    private String coverImgUUID;
    private String title;
    private String shortDesc;
    private String genre;
    private String createdDtm;


    public TrendingGamesModel() {

    }

    public TrendingGamesModel(String gameProfileUUID, String coverImgUUID, String title,
                              String shortDesc, String genre, String createdDtm) {

        this.gameProfileUUID = gameProfileUUID;
        this.coverImgUUID = coverImgUUID;
        this.title = title;
        this.shortDesc = shortDesc;
        this.genre = genre;
        this.createdDtm = createdDtm;
    }


    public String getGameProfileUUID() {
        return gameProfileUUID;
    }

    public String getCoverImgUUID() {
        return coverImgUUID;
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

    public String getCreatedDtm() {
        return createdDtm;
    }


    public void setGameProfileUUID(String gameProfileUUID) {
        this.gameProfileUUID = gameProfileUUID;
    }

    public void setCoverImgUUID(String coverImgUUID) {
        this.coverImgUUID = coverImgUUID;
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

    public void setCreatedDtm(String createdDtm) {
        this.createdDtm = createdDtm;
    }
}
