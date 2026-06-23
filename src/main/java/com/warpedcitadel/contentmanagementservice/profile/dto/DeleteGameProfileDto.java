package com.warpedcitadel.contentmanagementservice.profile.dto;

public record DeleteGameProfileDto(
        String userUUID,
        String gameProfileUUID
) {}
