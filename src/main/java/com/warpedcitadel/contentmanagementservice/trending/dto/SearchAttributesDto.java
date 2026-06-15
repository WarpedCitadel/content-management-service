package com.warpedcitadel.contentmanagementservice.trending.dto;

public record SearchAttributesDto(
        String title,
        Integer genre
) {
    public SearchAttributesDto {
        if (genre == null) {
            genre = -1;
        }
    }
}
