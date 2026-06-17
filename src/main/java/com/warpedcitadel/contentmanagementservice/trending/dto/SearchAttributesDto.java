package com.warpedcitadel.contentmanagementservice.trending.dto;

public record SearchAttributesDto(
        String title,
        Integer genre,
        String[] platformOS,
        Integer mostRecent,
        Integer gameType
) {
    public SearchAttributesDto {
        if (genre == null) {
            genre = -1;
        }
        if (mostRecent == null || mostRecent > 3 || mostRecent < 1 ) {
            mostRecent = -1;
        }
        if (gameType == null || gameType > 3 || gameType < 1 ) {
            gameType = -1;
        }
    }
}
