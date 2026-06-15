package com.warpedcitadel.contentmanagementservice.trending.model;

public class SearchAttributesModel {

    private String title;
    private int genre;


    public SearchAttributesModel(String title, int genre) {
        this.title = title;
        this.genre = genre;
    }


    public String getTitle() {
        return title;
    }

    public int getGenre() {
        return genre;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public void setGenre(int genre) {
        this.genre = genre;
    }
}
