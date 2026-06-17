package com.warpedcitadel.contentmanagementservice.trending.model;

public class SearchAttributesModel {

    private String title;
    private int genre;
    private String[] platformOS;


    public SearchAttributesModel() {

    }

    public SearchAttributesModel(String title, int genre, String[] platformOS) {
        this.title = title;
        this.genre = genre;
         this.platformOS = platformOS;
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


    public void setTitle(String title) {
        this.title = title;
    }

    public void setGenre(int genre) {
        this.genre = genre;
    }

    public void setPlatformOS(String[] platformOS) {
        this.platformOS = platformOS;
    }
}
