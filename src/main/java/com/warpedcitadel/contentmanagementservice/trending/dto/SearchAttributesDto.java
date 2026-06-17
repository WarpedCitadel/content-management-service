package com.warpedcitadel.contentmanagementservice.trending.dto;

public record SearchAttributesDto(
        String title,
        Integer genre,
        String[] platformOS
) {
    public SearchAttributesDto {
        if (genre == null) {
            genre = -1;
        }
    }
}
