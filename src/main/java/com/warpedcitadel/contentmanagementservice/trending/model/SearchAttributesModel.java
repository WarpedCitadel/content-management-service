package com.warpedcitadel.contentmanagementservice.trending.model;

public class SearchAttributesModel {

    private String title;
    private int genre;
    private String[] platformOS;
    private int mostRecent;
    private int gameType;


    public SearchAttributesModel() {

    }

    public SearchAttributesModel(String title, int genre, String[] platformOS, int mostRecent, int gameType) {
        this.title = title;
        this.genre = genre;
        this.platformOS = platformOS;
        this.mostRecent = mostRecent;
        this.gameType = gameType;
    }


    public String getTitle() {
        return title;
    }

    public int getGenre() {
        return genre;
    }

    public String[] getPlatformOS() {
        return platformOS;
    }

    public int getMostRecent() {
        return mostRecent;
    }

    public int getGameType() {
        return gameType;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public void setGenre(int genre) {
        this.genre = genre;
    }

    public void setPlatformOS(String[] platformOS) {
        this.platformOS = platformOS;
    }

    public void setMostRecent(int mostRecent) {
        this.mostRecent = mostRecent;
    }

    public void setGameType(int gameType) {
        this.gameType = gameType;
    }
}
