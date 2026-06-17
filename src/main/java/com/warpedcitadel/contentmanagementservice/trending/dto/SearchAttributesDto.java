package com.warpedcitadel.contentmanagementservice.trending.dto;

public record SearchAttributesDto(
        String title,
        Integer genre,
        String[] platformOS,
        Integer mostRecent
) {
    public SearchAttributesDto {
        if (genre == null) {
            genre = -1;
        }
        if (mostRecent == null || mostRecent > 3 || mostRecent < 1 ) {
            mostRecent = -1;
        }
    }
}
