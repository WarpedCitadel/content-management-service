package com.warpedcitadel.contentmanagementservice.profile.dto;

public record GameProfileDto(

        // todo: Apply data validation
        String userUUID,
        String title,
        String shortDesc,
        String description,
        int gameGenre,
        int gameType,
        Integer[] platformOS
) {}
