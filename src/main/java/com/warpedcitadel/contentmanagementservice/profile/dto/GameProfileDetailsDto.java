package com.warpedcitadel.contentmanagementservice.profile.dto;

import java.util.List;

public record GameProfileDetailsDto(
        String gameProfileUUID,
        String title,
        String description,
        String genreType,
        String gameType,
        List<String> platformOS,
        String createdDtm,
        GameProfileImageDto gameImages,
        GameFileDetailsDto gameFiles,
        String displayName,
        String userUUID
) {}
